package study.server.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;
import study.server.domain.basket.entity.Basket;
import study.server.domain.order.entity.Order;
import study.server.domain.user.dto.UserDto;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;
  private String email;
  private String password;
  private String address;
  private String phone;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
  private Basket basket;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Order> orders;

  public void updateUserName(String userName) {
    this.username = userName;
  }

  public void update(UserDto dto) {
    this.username = dto.getUsername();
    this.email = dto.getEmail();
    this.password = dto.getPassword();
    this.address = dto.getAddress();
    this.phone = dto.getPhone();
  }
}
