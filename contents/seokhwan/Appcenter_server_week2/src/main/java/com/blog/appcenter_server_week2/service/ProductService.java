package com.blog.appcenter_server_week2.service;

import com.blog.appcenter_server_week2.domain.entity.Product;
import com.blog.appcenter_server_week2.domain.repository.ProductRepository;
import com.blog.appcenter_server_week2.domain.repository.UserRepository;
import com.blog.appcenter_server_week2.dto.product.ProductListResponseDto;
import com.blog.appcenter_server_week2.dto.product.ProductUploadRequestDto;
import com.blog.appcenter_server_week2.dto.product.ProductUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public ProductUploadResponseDto addProduct(Long userId, ProductUploadRequestDto productUploadRequestDto) {
        Product product = Product.builder()
                .price(productUploadRequestDto.getPrice())
                .title(productUploadRequestDto.getTitle())
                .description(productUploadRequestDto.getDescription())
                .location(userRepository.findById(userId).get().getLocation())
                .productState(productUploadRequestDto.getProductState())
                .user(userRepository.findById(userId).get())
                .build();

        Product saveProduct = productRepository.save(product);

        return ProductUploadResponseDto.from(saveProduct);
    }


    @Transactional(readOnly = true)
    public List<ProductListResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductListResponseDto> productListResponseDtos = new ArrayList<>();

        for (Product product : products) {
            productListResponseDtos.add(ProductListResponseDto.from(product));
        }

        return productListResponseDtos;
    }


    public ProductUploadResponseDto updateProduct(Long postId, ProductUploadRequestDto productUploadRequestDto) {
        //예외처리 생략
        Product existingProduct = productRepository.findById(postId).orElse(null);

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


    public void deleteProduct(Long postId) {
        productRepository.deleteById(postId);
    }
}
