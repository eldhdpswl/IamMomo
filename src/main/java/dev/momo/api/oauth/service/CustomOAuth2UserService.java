package dev.momo.api.oauth.service;


import dev.momo.api.member.entity.User;
import dev.momo.api.member.repository.UserRepository;
import dev.momo.api.oauth.entity.ProviderType;
import dev.momo.api.oauth.entity.RoleType;
import dev.momo.api.oauth.entity.UserPrincipal;
import dev.momo.api.oauth.exception.OAuthProviderMissMatchException;
import dev.momo.api.oauth.info.OAuth2UserInfo;
import dev.momo.api.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /*
     * oauth의 과정이 전부 이뤄지고 난 다음에 loadUser 함수에 들어가는 순간, 사용자의 정보가 들어온다.
     * 사용자 정보를 확인하고 DB에 save 또는 update한다.
     * ( 그 다음 CustomUserDetailsService가 실행되는거같다 ???????)
     * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        // userRequest는 request 들어온 데이터

        // ProviderType 체크 (소셜로그인 - 카카오, 네이버, 구글 등등 체크)
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        // OAuth2UserInfoFactory는 소셜로그인 분기처리 진행
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId());

        // user 정보 데이터 유무 분기처리
        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
//            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    // 신규 회원일때 user정보를 생성한다.
    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();

        User user = new User(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                providerType,
                RoleType.USER,
                now,
                now,
                "1"
        );

        return userRepository.saveAndFlush(user);
    }

    // 가입된 회원일때 user정보를 update한다.
//    private User updateUser(User user, OAuth2UserInfo userInfo) {
//        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
//            user.setUsername(userInfo.getName());
//        }
//
//        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())) {
//            user.setProfileImageUrl(userInfo.getImageUrl());
//        }
//
//        return user;
//    }
}