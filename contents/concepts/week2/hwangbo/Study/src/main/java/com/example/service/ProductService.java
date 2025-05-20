package com.example.service;

import com.example.domain.product.Product;
import com.example.domain.product.ProductRepository;
import com.example.dto.product.ProductRequest;
import com.example.dto.product.ProductResponse;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        log.info("상품 생성 요청 → name={}, price={}, quantity={}",
                request.getName(), request.getPrice(), request.getQuantity());
        Product p = new Product(request.getName(), request.getPrice(), request.getQuantity());
        Product saved = productRepository.save(p);
        log.info("상품 생성 완료 → id={}", saved.getId());
        return ProductResponse.of(saved);
    }

    public void deleteProduct(Long id) {
        log.info("상품 삭제 요청 → id={}", id);
        if (!productRepository.existsById(id)) {
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }
        productRepository.deleteById(id);
        log.info("상품 삭제 완료 → id={}", id);
    }

    public ProductResponse updateQuantity(Long id, int quantity) {
        log.info("수량 수정 요청 → id={}, quantity={}", id, quantity);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.ITEM_NOT_FOUND));
        product.updateQuantity(quantity);
        log.info("수량 수정 완료 → id={}, newQuantity={}", id, product.getQuantity());
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        log.info("전체 상품 조회 요청");
        List<Product> all = productRepository.findAll();
        log.info("전체 상품 조회 완료 → count={}", all.size());
        return all.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id) {
        log.info("상품 조회 요청 → id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.ITEM_NOT_FOUND));
        log.info("상품 조회 완료 → id={}, name={}", id, product.getName());
        return ProductResponse.of(product);
    }


    // cartservice에서 사용하기 위한 메소드, findproductbyid 그대로 사용하면 cartItem 엔티티의 외래키 부분을 dto로 사용해야 하는 상황이 생김.
    @Transactional(readOnly = true)
    protected Product findProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
    }
}
