package com.example.service;

import com.example.DTO.product.ProductRequest;
import com.example.domain.product.Product;
import com.example.domain.product.ProductRepository;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest request) {
        log.info("상품 생성 요청 → name={}, price={}, quantity={}",
                request.getName(), request.getPrice(), request.getQuantity());
        Product p = new Product(request.getName(), request.getPrice(), request.getQuantity());
        Product saved = productRepository.save(p);
        log.info("상품 생성 완료 → id={}", saved.getId());
        return saved;
    }

    public void deleteProduct(Long id) {
        log.info("상품 삭제 요청 → id={}", id);
        if (!productRepository.existsById(id)) {
            log.warn("삭제 실패: 해당 상품 없음 → id={}", id);
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }
        productRepository.deleteById(id);
        log.info("상품 삭제 완료 → id={}", id);
    }

    public Product updateQuantity(Long id, int quantity) {
        log.info("수량 수정 요청 → id={}, quantity={}", id, quantity);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("수정 실패: 해당 상품 없음 → id={}", id);
                    return new CustomException(ErrorCode.ITEM_NOT_FOUND);
                });
        product.updateQuantity(quantity);
        log.info("수량 수정 완료 → id={}, newQuantity={}", id, product.getQuantity());
        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        log.info("전체 상품 조회 요청");
        List<Product> all = productRepository.findAll();
        log.info("전체 상품 조회 완료 → count={}", all.size());
        return all;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        log.info("상품 조회 요청 → id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("조회 실패: 해당 상품 없음 → id={}", id);
                    return new CustomException(ErrorCode.ITEM_NOT_FOUND);
                });
        log.info("상품 조회 완료 → id={}, name={}", id, product.getName());
        return product;
    }
}
