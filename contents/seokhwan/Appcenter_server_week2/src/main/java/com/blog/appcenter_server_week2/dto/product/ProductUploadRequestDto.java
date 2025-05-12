package com.blog.appcenter_server_week2.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUploadRequestDto {

    @NotNull(message = "가격을 입력해주세요.")
    private Long price;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100byte까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String description;

    @NotBlank
    private Integer productState;



}
