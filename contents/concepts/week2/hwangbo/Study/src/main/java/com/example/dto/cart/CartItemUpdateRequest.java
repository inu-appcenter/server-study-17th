package com.example.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "장바구니 아이템 수량 변경 요청")
@Getter
public class CartItemUpdateRequest {

    @NotBlank(message = "변경할 물품 수량을 입력해주세요.")
    @Schema(description = "변경할 수량", example = "3")
    private int quantity;
}

