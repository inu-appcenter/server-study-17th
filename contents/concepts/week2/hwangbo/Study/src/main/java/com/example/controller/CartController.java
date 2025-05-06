package com.example.controller;


import com.example.DTO.CartResponse;
import com.example.domain.user.User;
import com.example.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;

    // -> userid를 url에서 그대로 받아오면 다른 사용자가 url 변경해서 조회할 수도 있다고 함. 테스트 해봐야겠다
//    @GetMapping("/{userId}")
//    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
//        return ResponseEntity.ok(cartService.getCartByUserId(userId));
//    }

    @GetMapping("/{userid}")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    }
}
