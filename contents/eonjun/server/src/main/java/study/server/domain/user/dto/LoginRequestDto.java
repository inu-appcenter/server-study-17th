package study.server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원 로그인 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

  @Schema(
    description = "이메일",
    example = "qwer@naver.com"
  )
  @NotNull(message = "이메일 입력은 필수입니다.")
  private String email;

  @Schema(
    description = "비밀번호",
    example = "qwerqwer1@"
  )
  @NotNull(message = "패스워드 입력은 필수입니다.")
  private String password;
}
