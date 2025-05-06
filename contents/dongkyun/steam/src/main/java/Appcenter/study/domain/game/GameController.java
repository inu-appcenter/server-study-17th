package Appcenter.study.domain.game;

import Appcenter.study.global.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    // 게임 정보 불러오기
    @GetMapping("/info/{gameId}")
    public ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getGameInfo(gameId));
    }

    // 게임 환불
    @DeleteMapping("/{gameId}")
    public ResponseEntity<RefundResponse> refund(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.refund(userDetails, gameId));
    }
}
