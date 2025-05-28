package Appcenter.study.global.security.oauth2.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    NORMAL("normal"),
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    @JsonValue
    private final String registrationId;

    @JsonCreator
    public static OAuth2Provider fromOAuth2Provider(String value) {

        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .findAny()
                .orElse(null);
    }
}
