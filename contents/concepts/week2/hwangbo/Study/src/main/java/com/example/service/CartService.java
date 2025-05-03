package com.example.service;

import com.example.DTO.CartItemResponse;
import com.example.DTO.CartResponse;
import com.example.domain.cart.Cart;
import com.example.domain.cart.CartItem;
import com.example.domain.cart.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartResponse getCartByUserId(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        Cart cart = optionalCart
                .orElseThrow(() ->
                        new NoSuchElementException("Cart not found for user: " + userId));

        return CartResponse.from(cart);
    }



}
