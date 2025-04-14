package com.example.DTO;

import com.example.domain.cart.Cart;
import com.example.domain.cart.CartItem;
import com.example.domain.cart.CartRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;

    public static CartResponse from(Cart cart) {
        List<CartItemResponse> itemDtos = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            itemDtos.add(CartItemResponse.from(item));
        }

        return new CartResponse(itemDtos);
    }
}
