package Appcenter.study.global.security.oauth2.user;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getName();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getNickname();

    String getProfileUrl();
}
