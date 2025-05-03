package com.blog.appcenter_server_week2.dto.product;

import com.blog.appcenter_server_week2.domain.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductUploadResponseDto {

    private final Long key;

    @Builder
    private ProductUploadResponseDto(Long key) {
        this.key = key;
    }

    public static ProductUploadResponseDto from(Product product) {
        return ProductUploadResponseDto.builder()
                .key(product.getPostId())
                .build();
    }
}
