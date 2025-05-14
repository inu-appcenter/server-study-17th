package Appcenter.study.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {

    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Schema(description = "사용자의 이메일 주소", example = "user1234@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    @Schema(description = "사용자의 비밀번호", example = "password123!")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}