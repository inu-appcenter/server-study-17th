package com.blog.appcenter_server_week2.dto.product;

import com.blog.appcenter_server_week2.domain.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductListResponseDto {

    private Long userId;

    private Long price;

    private String title;

    private String description;

    private String location;

    private Long heart;

    private int productState;

    @Builder
    private ProductListResponseDto(Long userId, Long price, String title, String description, String location, Long heart, int productState) {
        this.userId = userId;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = location;
        this.heart = heart;
        this.productState = productState;
    }

    public static ProductListResponseDto from(Product product) {
        return ProductListResponseDto.builder()
                .userId(product.getUser().getId())
                .price(product.getPrice())
                .title(product.getTitle())
                .description(product.getDescription())
                .location(product.getLocation())
                .heart(product.getHeart())
                .productState(product.getProductState())
                .build();
    }



}
