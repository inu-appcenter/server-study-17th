package Appcenter.study.domain.member.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원가입 응답 DTO")
public class SignupResponseDto {

    @Schema(description = "회원가입된 이메일 주소", example = "user@example.com")
    private final String email;

    @Schema(description = "회원가입된 닉네임", example = "johndoe")
    private final String nickname;
}
