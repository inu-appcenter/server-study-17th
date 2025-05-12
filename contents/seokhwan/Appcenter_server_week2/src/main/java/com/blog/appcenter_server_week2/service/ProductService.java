package com.blog.appcenter_server_week2.service;

import com.blog.appcenter_server_week2.domain.entity.Product;
import com.blog.appcenter_server_week2.domain.entity.User;
import com.blog.appcenter_server_week2.domain.repository.ProductRepository;
import com.blog.appcenter_server_week2.domain.repository.UserRepository;
import com.blog.appcenter_server_week2.dto.product.ProductListResponseDto;
import com.blog.appcenter_server_week2.dto.product.ProductUploadRequestDto;
import com.blog.appcenter_server_week2.dto.product.ProductUploadResponseDto;
import com.blog.appcenter_server_week2.exception.CustomException;
import com.blog.appcenter_server_week2.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public ProductUploadResponseDto addProduct(Long userId, ProductUploadRequestDto productUploadRequestDto) {
        log.info("게시글 등록 시작 - 사용자 ID: {}, 상품명: {}", userId, productUploadRequestDto.getTitle());

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("게시글 등록 실패 - 존재하지 않는 사용자 ID: {}", userId);
                    return new CustomException(ErrorCode.NOT_EXIST_ID);
                });

        Product product = Product.builder()
                .price(productUploadRequestDto.getPrice())
                .title(productUploadRequestDto.getTitle())
                .description(productUploadRequestDto.getDescription())
                .location(user.getLocation())
                .productState(productUploadRequestDto.getProductState())
                .user(user)
                .build();
        log.debug("게시글 생성 완료 - 상품명: {}, 가격: {}, 상태: {}", product.getTitle(), product.getPrice(), product.getProductState());

        // 게시글 저장
        Product saveProduct = productRepository.save(product);
        log.debug("게시글 저장 완료 - 게시글 ID: {}", saveProduct.getPostId());

        return ProductUploadResponseDto.from(saveProduct);
    }


    @Transactional(readOnly = true)
    public List<ProductListResponseDto> getProducts() {
        log.info("게시글 조회 시작");
        //동적 쿼리 써보기
        return productRepository.findAll().stream()
                .map(ProductListResponseDto::from)
                .toList();
    }


    public ProductUploadResponseDto updateProduct(Long postId, ProductUploadRequestDto productUploadRequestDto) {
        log.info("게시글 정보 업데이트 시작 - 게시글 ID: {}", postId);
        // 상품 조회
        Product existingProduct = productRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("게시글 업데이트 실패 - 존재하지 않는 게시글 ID: {}", postId);
                    return new CustomException(ErrorCode.VALIDATION_ERROR);
                });

        log.debug("업데이트 전 게시글 정보 - 상품 ID: {}, 제목: {}, 가격: {}, 상태: {}", existingProduct.getPostId(), existingProduct.getTitle(), existingProduct.getPrice(), existingProduct.getProductState());

        existingProduct.update(
                productUploadRequestDto.getPrice(),
                productUploadRequestDto.getTitle(),
                productUploadRequestDto.getDescription(),
                existingProduct.getLocation(),
                existingProduct.getHeart(),
                productUploadRequestDto.getProductState(),
                existingProduct.getUser()
        );

        return ProductUploadResponseDto.from(existingProduct);
    }


    public void deleteProduct(Long postId, Long userId) {
        // 상품 조회 및 소유자 검증
        Product product = productRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> {
                    log.warn("상품 삭제 실패 - 상품이 존재하지 않거나 삭제 권한 없음 - 상품 ID: {}, 사용자 ID: {}", postId, userId);
                    return new CustomException(ErrorCode.VALIDATION_ERROR);
                });

        log.debug("삭제 대상 상품 정보 - 상품 ID: {}, 제목: {}, 가격: {}, 판매자 ID: {}", product.getPostId(), product.getTitle(), product.getPrice(), product.getUser().getId());

        // 상품 삭제
        productRepository.deleteById(postId);

        log.info("게시글 삭제 완료 - 상품 ID: {}, 제목: {}", postId, product.getTitle());
    }
}
