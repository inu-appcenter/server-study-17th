package study.server.domain.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;
import study.server.domain.order.entity.OrderDetail;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Delivery extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_detail_id")
  private OrderDetail orderDetail;

  private long orderId;
  private long productId;
  private long userId;

  private String deliveryComp;
}
