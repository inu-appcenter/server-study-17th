package Appcenter.study.dto.res;

import Appcenter.study.entity.Game;
import Appcenter.study.entity.Review;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GameInfoResponse {

    private final String title;
    private final String genre;
    private final LocalDateTime createAt;
    private final String developer;
    private final String publisher;
    private final Integer price;
    private final Double rate;
    private final List<Review> reviews;

    @Builder
    private GameInfoResponse(Game game) {
        this.title = game.getTitle();
        this.genre = game.getGenre();
        this.createAt = game.getCreateAt();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
        this.price = game.getPrice();
        this.rate = game.getRate();
        this.reviews = game.getReviews();
    }
}
