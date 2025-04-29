package study.server.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.server.domain.user.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;

  private String address;

  @NotBlank(message = "이메일은 필수 입력 값입니다.")
  @Email(message = "이메일 형식에 맞지 않습니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$")
  private String password;


  private String phone;
  private Role role;
}
