package com.example.controller;


import com.example.DTO.CartResponse;
import com.example.security.CustomUserDetail;
import com.example.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;
    
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        return ResponseEntity.ok(cartService.getCartByUserId(customUserDetail.getId()));
    }
}
