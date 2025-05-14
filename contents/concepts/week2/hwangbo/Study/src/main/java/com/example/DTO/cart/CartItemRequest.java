package com.example.DTO.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "장바구니에 추가할 상품 정보를 담는 요청")
@Getter
public class CartItemRequest {

    @NotNull
    @Schema(description = "추가할 상품의 ID", example = "42")
    private Long productId;

    @NotNull
    @Schema(description = "추가할 상품의 수량", example = "2")
    private int quantity;
}
