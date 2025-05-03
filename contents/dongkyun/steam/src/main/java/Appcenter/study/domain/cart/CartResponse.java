package Appcenter.study.domain.cart;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CartResponse {

    private final String memberNickname;
    private final String gameTitle;
    private final LocalDateTime createAt;

    @Builder
    private CartResponse(Cart cart) {
        this.memberNickname = cart.getMember().getNickname();
        this.gameTitle = cart.getGame().getTitle();
        this.createAt = cart.getCreateAt();
    }
}
