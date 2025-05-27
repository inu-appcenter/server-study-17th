package Appcenter.study.domain.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "환불 응답 DTO")
public class RefundResponse {

    @Schema(description = "환불 요청한 회원의 닉네임", example = "johndoe")
    private final String memberNickname;

    @Schema(description = "환불된 게임의 제목", example = "엘든 링")
    private final String gameTitle;

    @Schema(description = "환불 처리 시간 (요청 시각)", example = "2025-05-13T15:40:00")
    private final LocalDateTime refundTime;

    @Builder
    private RefundResponse(String memberNickname, String gameTitle) {
        this.memberNickname = memberNickname;
        this.gameTitle = gameTitle;
        this.refundTime = LocalDateTime.now(); // 생성 시점 기준
    }
}