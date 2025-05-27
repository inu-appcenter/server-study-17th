package com.example.api;

import com.example.dto.cart.CartItemRequest;
import com.example.dto.cart.CartItemUpdateRequest;
import com.example.dto.cart.CartResponse;
import com.example.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cart", description = "장바구니 관련 API")
public interface CartApiSpecification {

    @Operation(summary = "장바구니 조회", description = "현재 사용자 장바구니 정보를 반환")
    ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    );

    @Operation(summary = "장바구니에 상품 추가", description = "사용자의 장바구니에 상품을 추가하고 업데이트된 장바구니 정보를 반환")
    ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody @Valid CartItemRequest request
    );

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니 아이템 ID와 새로운 수량으로 상품 수량을 수정")
    ResponseEntity<CartResponse> updateItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable("itemId") Long itemId,
            @RequestBody @Valid CartItemUpdateRequest request
    );

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니 아이템 ID로 상품을 삭제")
    ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal CustomUserDetail user,
            @PathVariable("itemId") Long itemId
    );
}
