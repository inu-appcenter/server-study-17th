package Appcenter.study.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "장바구니 응답 DTO")
public class CartResponse {

    @Schema(description = "장바구니를 추가한 회원의 닉네임", example = "johndoe")
    private final String memberNickname;

    @Schema(description = "장바구니에 담긴 게임 제목", example = "엘든 링")
    private final String gameTitle;

    @Schema(description = "장바구니에 담은 시각 (생성 시각)", example = "2025-05-13T15:30:00")
    private final LocalDateTime createAt;

    @Builder
    private CartResponse(Cart cart) {
        this.memberNickname = cart.getMember().getNickname();
        this.gameTitle = cart.getGame().getTitle();
        this.createAt = cart.getCreateAt();
    }
}
