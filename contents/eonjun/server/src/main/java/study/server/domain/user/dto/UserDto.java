package study.server.domain.user.dto;

import lombok.*;
import study.server.domain.basic.BaseEntity;

@Getter
public class UserDto extends BaseEntity {


  private String username;
  private String email;
  private String password;
  private String address;
  private String phone;

  @Builder
  public UserDto(String username, String email, String password, String address, String phone) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phone = phone;
  }
}

