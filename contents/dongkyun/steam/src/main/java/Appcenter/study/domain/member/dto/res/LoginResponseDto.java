package Appcenter.study.domain.member.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class LoginResponseDto {

    private final String accessToken;
    private final Long accessTokenExpires;
    private final Date accessTokenExpiresDate;
}
