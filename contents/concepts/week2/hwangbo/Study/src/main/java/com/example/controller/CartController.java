package com.example.controller;

import com.example.api.CartApiSpecification;
import com.example.dto.cart.CartItemRequest;
import com.example.dto.cart.CartItemUpdateRequest;
import com.example.dto.cart.CartResponse;
import com.example.security.CustomUserDetail;
import com.example.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController implements CartApiSpecification {

    private final CartService cartService;

    @Override
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        return ResponseEntity.ok(cartService.getCartByUserId(customUserDetail.getId()));
    }

    @Override
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody @Valid CartItemRequest request) {
        CartResponse response = cartService.addProductToCart(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long itemId,
            @RequestBody @Valid CartItemUpdateRequest request) {
        CartResponse resp = cartService.updateCartItem(user.getId(), itemId, request);
        return ResponseEntity.ok(resp);
    }

    @Override
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable Long itemId) {
        cartService.removeCartItem(user.getId(), itemId);
        return ResponseEntity.noContent().build();
    }
}
