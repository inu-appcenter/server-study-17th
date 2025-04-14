package Appcenter.study.controller;

import Appcenter.study.dto.res.GameInfoResponse;
import Appcenter.study.dto.res.RefundResponse;
import Appcenter.study.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // 게임 정보 불러오기
    @GetMapping("/game/info/{gameId}")
    public ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getGameInfo(gameId));
    }

    // 게임 환불
    @DeleteMapping("/refund/{memberId}/{gameId}")
    public ResponseEntity<RefundResponse> refund(@PathVariable Long memberId, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.refund(memberId, gameId));
    }
}
