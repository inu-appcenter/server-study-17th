package Appcenter.study.global.security.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final String profileUrl;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes, String accessToken) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        this.attributes = profile;
        this.accessToken = accessToken;
        this.id = ((Long) attributes.get("id")).toString();
        this.email = (String) kakaoAccount.get("email");
        this.name = null;
        this.firstName = null;
        this.lastName = null;
        this.nickname = (String) attributes.get("nickname");
        this.profileUrl = null;

        this.attributes.put("id", id);
        this.attributes.put("email", this.email);
    }

    @Override
    public OAuth2Provider getProvider() { return OAuth2Provider.KAKAO; }

    @Override
    public String getAccessToken() { return accessToken; }

    @Override
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public String getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getEmail() { return email; }

    @Override
    public String getFirstName() { return firstName; }

    @Override
    public String getLastName() { return lastName; }

    @Override
    public String getNickname() { return nickname; }

    @Override
    public String getProfileUrl() { return profileUrl; }
}
