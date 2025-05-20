package study.server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import study.server.domain.user.entity.Role;

@Schema(description = "회원 가입 DTO")
@Getter
@NoArgsConstructor
public class UserDto {

  @Schema(
    description = "이름",
    example = "홍길동"
  )
  @NotBlank(message = "이름은 필수입니다.")
  private String username;

  @Schema(
    description = "이메일",
    example = "qwer@naver.com"
  )
  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @Schema(
    description = "비밀번호",
    example = "qwerqwer1@"
  )
  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
  private String password;

  @Schema(
    description = "주소",
    example = "텍사스"
  )
  @NotBlank(message = "주소는 필수입니다.")
  private String address;

  @Schema(
    description = "전화 번호",
    example = "01054920783"
  )
  @NotBlank(message = "전화번호는 필수입니다.")
  @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 숫자만 10~11자리로 입력해주세요.")
  private String phone;

  @Schema(
    description = "역할",
    example = "USER/ADMIN"
  )
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
