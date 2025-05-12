package study.server.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import study.server.global.common.BaseEntity;
import study.server.domain.product.entity.Product;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  private long userId;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private Rating rating;
}
