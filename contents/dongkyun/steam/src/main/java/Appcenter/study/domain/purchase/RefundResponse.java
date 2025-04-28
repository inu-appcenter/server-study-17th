package Appcenter.study.domain.purchase;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefundResponse {

    private final String memberNickname;
    private final String gameTitle;
    private final LocalDateTime refundTime;

    @Builder
    private RefundResponse(String memberNickname, String gameTitle) {
        this.memberNickname = memberNickname;
        this.gameTitle = gameTitle;
        this.refundTime = LocalDateTime.now();
    }
}
