package study.server.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import study.server.domain.user.entity.Role;

@Getter
@NoArgsConstructor
public class UserDto {

  @NotBlank(message = "이름은 필수입니다.")
  private String username;

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
  private String password;

  @NotBlank(message = "주소는 필수입니다.")
  private String address;

  @NotBlank(message = "전화번호는 필수입니다.")
  @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 숫자만 10~11자리로 입력해주세요.")
  private String phone;

  private Role role;

  @Builder
  public UserDto(String username, String email, String password, String address, String phone, Role role) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phone = phone;
    this.role = role;
  }
}
