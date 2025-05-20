package com.example.service;

import com.example.domain.cart.Cart;
import com.example.domain.cart.CartItem;
import com.example.domain.cart.CartRepository;
import com.example.domain.product.Product;
import com.example.dto.cart.CartItemRequest;
import com.example.dto.cart.CartItemUpdateRequest;
import com.example.dto.cart.CartResponse;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService  productService;

    public CartResponse getCartByUserId(Long userId) {
        log.info("장바구니 조회 요청 → userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        log.info("장바구니 조회 완료 → userId={}, items={}", userId, cart.getItems().size());
        return CartResponse.from(cart);
    }

    public CartResponse addProductToCart(Long userId, CartItemRequest req) {
        log.info("장바구니 상품 추가 요청 → userId={}, productId={}, quantity={}",
                userId, req.getProductId(), req.getQuantity());
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Product product = productService.findProductEntityById(req.getProductId());

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();

        // 기존에 존재하던 상품인 경우
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQuantity = item.getQuantity() + req.getQuantity();
            item.updateQuantity(newQuantity);
            log.info("기존 품목 수량 합산 → itemId={}, newQuantity={}", item.getId(), newQuantity);
        }

        // 기존에 존재하지 않는 상품인 경우
        else {
            CartItem item = new CartItem(product, cart, req.getQuantity());
            cart.getItems().add(item);
            log.info("신규 품목 추가 → productId={}, itemId={}, quantity={}",
                    product.getId(), item.getId(), req.getQuantity());
        }

        log.info("장바구니 최종 상태 → userId={}, totalItems={}", userId, cart.getItems().size());
        return CartResponse.from(cart);
    }

    public CartResponse updateCartItem(Long userId, Long itemId, CartItemUpdateRequest req) {
        log.info("장바구니 품목 수량 수정 요청 → userId={}, itemId={}, quantity={}",
                userId, itemId, req.getQuantity());
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
        item.updateQuantity(req.getQuantity());
        log.info("장바구니 품목 수량 수정 완료 → itemId={}, newQuantity={}",
                itemId, req.getQuantity());
        return CartResponse.from(cart);
    }

    public void removeCartItem(Long userId, Long itemId) {
        log.info("장바구니 품목 삭제 요청 → userId={}, itemId={}", userId, itemId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
        cart.getItems().remove(item);
        log.info("장바구니 품목 삭제 완료 → itemId={}, remainingItems={}",
                itemId, cart.getItems().size());
    }


}
