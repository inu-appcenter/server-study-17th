package study.server.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;
import study.server.domain.delivery.entity.Delivery;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDetail extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderDetail")
  private Delivery delivery;

  private long productId;

  private long userId;

  private long totalPrice;
}
