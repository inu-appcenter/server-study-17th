package study.server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import study.server.domain.user.entity.Role;

@Getter
@NoArgsConstructor
public class UserResponseDto {

  @Schema(
    description = "이름",
    example = "홍길동"
  )
  private String username;

  @Schema(
    description = "이메일",
    example = "qwer@naver.com"
  )
  private String email;

  @Schema(
    description = "주소",
    example = "텍사스"
  )
  private String address;

  @Schema(
    description = "전화 번호",
    example = "01054920783"
  )
  private String phone;

  @Schema(
    description = "역할",
    example = "USER/ADMIN"
  )
  private Role role;

  @Builder
  public UserResponseDto(String username, String email, String address, String phone, Role role) {
    this.username = username;
    this.email = email;
    this.address = address;
    this.phone = phone;
    this.role = role;
  }
}

