package com.blog.appcenter_server_week2.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUploadRequestDto {

    @NotBlank
    private Long price;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private int productState;



}
