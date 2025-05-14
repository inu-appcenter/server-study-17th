package com.example.controller;


import com.example.DTO.cart.CartItemRequest;
import com.example.DTO.cart.CartItemUpdateRequest;
import com.example.DTO.cart.CartResponse;
import com.example.security.CustomUserDetail;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Cart", description = "장바구니 관련 API")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "현재 사용자 장바구니 정보를 반환")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetail customUserDetail) {
        return ResponseEntity.ok(cartService.getCartByUserId(customUserDetail.getId()));
    }

    @Operation(summary = "장바구니에 상품 추가", description = "사용자의 장바구니에 상품을 추가하고 업데이트된 장바구니 정보를 반환")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetail user,
            @Parameter(description = "추가할 상품 정보", required = true)
            @RequestBody @Valid CartItemRequest request) {
        CartResponse response = cartService.addProductToCart(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니 아이템 ID와 새로운 수량으로 상품 수량을 수정")
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetail user,
            @Parameter(description = "수정할 장바구니 아이템 ID", required = true, example = "1")
            @PathVariable Long itemId,
            @Parameter(description = "수정할 수량 정보", required = true)
            @RequestBody @Valid CartItemUpdateRequest request) {
        CartResponse resp = cartService.updateCartItem(user.getId(), itemId, request);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니 아이템 ID로 상품을 삭제")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetail user,
            @Parameter(description = "삭제할 장바구니 아이템 ID", required = true, example = "1")
            @PathVariable Long itemId) {
        cartService.removeCartItem(user.getId(), itemId);
        return ResponseEntity.noContent().build();
    }
}
