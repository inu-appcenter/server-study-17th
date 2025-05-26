package Appcenter.study.domain.game;

import Appcenter.study.domain.cart.Cart;
import Appcenter.study.domain.purchase.Purchase;
import Appcenter.study.domain.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 20)
    private String genre;

    @CreatedDate
    private LocalDateTime createAt;

    @Column(nullable = false, length = 30)
    private String developer;

    @Column(nullable = false, length = 30)
    private String publisher;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Double rate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts;

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }
    public List<Purchase> getPurchases() {
        return Collections.unmodifiableList(purchases);
    }
    public List<Cart> getCarts() {
        return Collections.unmodifiableList(carts);
    }
}
