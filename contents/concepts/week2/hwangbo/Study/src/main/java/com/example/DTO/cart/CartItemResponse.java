package com.example.DTO.cart;

import com.example.domain.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private String productName;
    private int quantity;
    private int price;

    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getPrice()
        );
    }
}
