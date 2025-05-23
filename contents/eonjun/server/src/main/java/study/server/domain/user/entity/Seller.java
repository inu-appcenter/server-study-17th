package study.server.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;
import study.server.domain.product.entity.Product;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Seller extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private String email;
  private String phone;
  private String password;

  @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Product> products;

  // 사업자 등록증
  private String certificate;
}
