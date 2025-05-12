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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public ProductUploadResponseDto addProduct(Long userId, ProductUploadRequestDto productUploadRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_ID));

        Product product = Product.builder()
                .price(productUploadRequestDto.getPrice())
                .title(productUploadRequestDto.getTitle())
                .description(productUploadRequestDto.getDescription())
                .location(user.getLocation())
                .productState(productUploadRequestDto.getProductState())
                .user(user)
                .build();

        Product saveProduct = productRepository.save(product);

        return ProductUploadResponseDto.from(saveProduct);
    }


    @Transactional(readOnly = true)
    public List<ProductListResponseDto> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductListResponseDto::from)
                .toList();
    }


    public ProductUploadResponseDto updateProduct(Long postId, ProductUploadRequestDto productUploadRequestDto) {
        Product existingProduct = productRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.VALIDATION_ERROR));

        Product saveProduct = existingProduct.update(
                productUploadRequestDto.getPrice(),
                productUploadRequestDto.getTitle(),
                productUploadRequestDto.getDescription(),
                existingProduct.getLocation(),
                existingProduct.getHeart(),
                productUploadRequestDto.getProductState(),
                existingProduct.getUser()
        );

        return ProductUploadResponseDto.from(saveProduct);
    }


    public void deleteProduct(Long postId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_ID));
        productRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.VALIDATION_ERROR));
        productRepository.deleteById(postId);
    }
}
