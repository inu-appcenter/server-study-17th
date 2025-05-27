package com.example.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "상품 수량 변경 요청 모델")
@Getter
public class ProductQuantityUpdateRequest {

    @NotBlank(message = "변경할 상품 수량을 입력해주세요.")
    @Schema(description = "변경할 상품 수량", example = "5")
    private int quantity;
}
