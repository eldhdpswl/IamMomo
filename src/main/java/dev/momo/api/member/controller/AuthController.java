package dev.momo.api.member.controller;


import dev.momo.api.global.oauth2_common.ApiResponse;
import dev.momo.api.global.oauth2_common.BasicResponse;
import dev.momo.api.global.properties.AppProperties;
import dev.momo.api.global.redis.RedisService;
import dev.momo.api.member.entity.AuthReqModel;
import dev.momo.api.member.entity.UserRefreshToken;
import dev.momo.api.member.repository.UserRefreshTokenRepository;
import dev.momo.api.member.service.UserService;
import dev.momo.api.oauth.entity.RoleType;
import dev.momo.api.oauth.entity.UserPrincipal;
import dev.momo.api.oauth.token.AuthToken;
import dev.momo.api.oauth.token.AuthTokenProvider;
import dev.momo.api.utils.CookieUtil;
import dev.momo.api.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.time.Duration;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    private final UserService userService;
    private final RedisService redisService;

    @PostMapping("/login")
    public ApiResponse login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {

        userService.checkUser(authReqModel.getEmail(), authReqModel.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getEmail(),
                        authReqModel.getPassword()
                )
        );

        log.info("authentication : " + authentication);

        String userId = authReqModel.getEmail();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // access token ??????
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        log.info("accessToken : " + accessToken.getToken());

        // refresh token ??????
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        log.info("refreshToken : " + refreshToken.getToken());

        redisService.setValues("RT:"+ userId, refreshToken.getToken(), Duration.ofMillis(now.getTime() + refreshTokenExpiry));

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return ApiResponse.success("token", accessToken.getToken());
    }

    @GetMapping("/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token ??????
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        // expired access token ?????? ?????? = (?????? ???????????? Not expired token yet.)
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return ApiResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        log.info("userId : "+ userId);

        // refresh token ??????
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        //refresh token ????????? ??????
        if (!authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }
        log.info("validate check");


        // redis?????? refresh token ??????
        String saved_refreshToken = redisService.getValues("RT:" + userId);
        if (ObjectUtils.isEmpty(saved_refreshToken)){
            return ApiResponse.invalidRefreshToken();
        }
        
        // access token ?????? ??????
        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // refresh token ?????? ??????
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        authRefreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // RefreshToken Redis ????????????
        redisService.setValues("RT:"+ userId, authRefreshToken.getToken(), Duration.ofMillis(now.getTime() + refreshTokenExpiry));


        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);

        return ApiResponse.success("token", newAccessToken.getToken());
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request, HttpServletResponse response){
        // access token ??????
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        Date now = new Date();

        // access token ????????? ??????
        if (!authToken.validate()) {
            return ApiResponse.invalidAccessToken();
        }

        long validTime = authToken.getTokenClaims().getExpiration().getTime() - now.getTime();
        
        // userId ???????????? ?????? ??????
        Claims claims = authToken.getTokenClaims();
        String userId = claims.getSubject();

        // refresh token ??????
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        //refresh token ????????? ??????
        if (!authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }

        // redis?????? refresh token ??????
        String saved_refreshToken = redisService.getValues("RT:" + userId);
        if (ObjectUtils.isEmpty(saved_refreshToken)){
            return ApiResponse.invalidRefreshToken();
        }
    
        // refresh token ??????
        redisService.deleteValues("RT:" + userId);

        redisService.setValues(accessToken, "Logout", Duration.ofMillis(validTime));

        // ???????????? refresh token ??????
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);

        return ApiResponse.logout();

    }



}