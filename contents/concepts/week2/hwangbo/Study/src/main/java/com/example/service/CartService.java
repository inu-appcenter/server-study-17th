package com.example.service;

import com.example.DTO.CartResponse;
import com.example.domain.cart.Cart;
import com.example.domain.cart.CartRepository;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return CartResponse.from(cart);
    }



}
