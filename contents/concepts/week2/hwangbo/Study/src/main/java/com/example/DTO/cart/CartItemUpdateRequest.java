package com.example.DTO.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "장바구니 아이템 수량 변경 요청")
@Getter
public class CartItemUpdateRequest {

    @NotNull
    @Schema(description = "변경할 수량", example = "3")
    private int quantity;
}

