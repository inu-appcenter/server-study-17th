package Appcenter.study.domain.game;

import Appcenter.study.global.security.jwt.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Game", description = "게임 관련 API")
public interface GameApiSpecification {

    @Operation(
            summary = "게임 정보 조회",
            description = "특정 게임의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게임 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GameInfoResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
            }
    )
    ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable Long gameId);

    @Operation(
            summary = "게임 환불",
            description = "현재 로그인한 사용자가 특정 게임을 환불합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "환불 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RefundResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
                    @ApiResponse(responseCode = "404", description = "해당 구매 정보를 찾을 수 없음")
            }
    )
    ResponseEntity<RefundResponse> refund(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long gameId);
}
