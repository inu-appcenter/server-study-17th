package Appcenter.study.global.security.oauth2.user;


import Appcenter.study.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes, String accessToken) {

        // if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) return new GoogleOAuth2UserInfo(attributes, accessToken);
        if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) return new KakaoOAuth2UserInfo(attributes, accessToken);
        // if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) return new NaverOAuth2UserInfo(attributes, accessToken);

        throw new OAuth2AuthenticationProcessingException("Invalid Provider With " + registrationId);
    }
}
