package Appcenter.study.controller;

import Appcenter.study.dto.res.CartResponse;
import Appcenter.study.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/cart/{memberId}/{gameId}")
    public ResponseEntity<CartResponse> addCart(@PathVariable Long memberId, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCart(memberId, gameId));
    }
}
