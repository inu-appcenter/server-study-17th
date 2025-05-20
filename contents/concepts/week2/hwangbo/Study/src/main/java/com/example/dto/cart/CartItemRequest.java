package com.example.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "장바구니에 추가할 상품 정보를 담는 요청")
@Getter
public class CartItemRequest {


    // 이 부분은 사용자가 Id를 직접 입력하는 방식이 아닐텐데 NotNull이 필요할까?
    // -> 프론트에서 실수로 id값 빼먹는 경우, 해커가 백엔드 주소를 알아낸 경우 id를 null값으로 반복해 넘긴다면?
    // -> 근데 id를 null값으로 반복해 넘긴다고 해도, 유효성 검사를 하나 안하나 서버에 부하가 가는 건 똑같지 않나? 알아보자
    @NotBlank(message = "상품의 Id를 입력해주세요.")
    @Schema(description = "추가할 상품의 ID", example = "42")
    private Long productId;


    @NotBlank(message = "상품의 수량을 입력해주세요.")
    @Schema(description = "추가할 상품의 수량", example = "2")
    private int quantity;
}
