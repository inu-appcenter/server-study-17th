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

    // 이거 뭔가뭔가임.. 그냥 빈 장바구니 반환하면 되지 왜 예외를 터뜨리게 해놨을까
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("cart not found for user: " + userId));

        return CartResponse.from(cart);
    }



}
