package study.server.domain.user.dto;

import lombok.*;
import study.server.domain.basic.BaseEntity;
import study.server.domain.user.entity.Role;

@Getter
@NoArgsConstructor
public class UserDto {


  private String username;
  private String email;
  private String password;
  private String address;
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

