package dev.momo.api.oauth.filter;


import dev.momo.api.oauth.token.AuthToken;
import dev.momo.api.oauth.token.AuthTokenProvider;
import dev.momo.api.utils.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        String tokenStr = HeaderUtil.getAccessToken(request); // 토큰에 필요한 정보를 가져온다.
        AuthToken token = tokenProvider.convertAuthToken(tokenStr); //토큰으로 변환하는 작업

        if (token.validate()) { //토큰 validation 체크
            Authentication authentication = tokenProvider.getAuthentication(token); // 사용자 정보와 token 등의 정보를 가져온다.
            // 사용자 정보를 가진 Authentication 객체를 SecurityContextHolder에 담은 이후 AuthenticationSuccessHandle를 실행한다.
            // (실패시 AuthenticationFailureHandler를 실행한다.)
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }

}

