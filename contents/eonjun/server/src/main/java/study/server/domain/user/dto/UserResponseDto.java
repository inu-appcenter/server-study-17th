package study.server.domain.user.dto;

import lombok.*;
import study.server.domain.user.entity.Role;

@Getter
@NoArgsConstructor
public class UserResponseDto {


  private String username;
  private String email;
  private String address;
  private String phone;
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

