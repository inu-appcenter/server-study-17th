package Appcenter.study.controller;

import Appcenter.study.dto.req.UpdateEmailRequest;
import Appcenter.study.dto.req.UpdateMypageRequest;
import Appcenter.study.dto.res.*;
import Appcenter.study.service.TotalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TotalController {

    private final TotalService totalService;

    // 게임 정보 불러오기
    @GetMapping("/game/info/{gameId}")
    public ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(totalService.getGameInfo(gameId));
    }

    // 장바구니 추가
    @PostMapping("/cart/{memberId}/{gameId}")
    public ResponseEntity<CartResponse> addCart(@PathVariable Long memberId, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(totalService.addCart(memberId, gameId));
    }

    // 마이페이지 수정
    @PutMapping("/mypage/update/{memberId}")
    public ResponseEntity<UpdateMypageResponse> updateMypage(@PathVariable Long memberId,@Valid @RequestBody UpdateMypageRequest updateMypageRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(totalService.updateMypage(memberId, updateMypageRequest));
    }

    // 이메일 변경
    @PatchMapping("/email/update/{memberId}")
    public ResponseEntity<UpdateEmailResponse> updateEmail(@PathVariable Long memberId, @Valid @RequestBody UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(totalService.updateEmail(memberId, updateEmailRequest));
    }

    // 게임 환불
    @DeleteMapping("/refund/{memberId}/{gameId}")
    public ResponseEntity<RefundResponse> refund(@PathVariable Long memberId, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(totalService.refund(memberId, gameId));
    }
}
