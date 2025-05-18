package com.example.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "상품 생성 요청 모델")
@Getter
public class ProductRequest {

    @NotBlank(message = "상품명을 입력해주세요.")
    @Schema(description = "상품명", example = "신형 블루투스 스피커")
    private String name;


    @NotBlank(message = "상품 가격을 입력해주세요.")
    @Schema(description = "상품 가격", example = "150000")
    private Integer price;


    @NotBlank(message = "상품의 수량을 입력해주세요.")
    @Schema(description = "상품 수량", example = "10")
    private Integer quantity;
}

