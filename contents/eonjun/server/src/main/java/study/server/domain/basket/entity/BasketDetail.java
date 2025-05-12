package study.server.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BasketDetail extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "basket_id")
  private Basket basket;

  private long productId;

  private long count;
}
