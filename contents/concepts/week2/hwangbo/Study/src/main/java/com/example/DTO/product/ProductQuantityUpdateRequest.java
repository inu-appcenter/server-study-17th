package com.example.DTO.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "상품 수량 변경 요청 모델")
@Getter
public class ProductQuantityUpdateRequest {

    @NotNull
    @Schema(description = "변경할 상품 수량", example = "5")
    private int quantity;
}
