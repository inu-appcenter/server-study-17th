package com.example.ticketing.person.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PersonUpdateRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(example = "홍길동")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Schema(example = "test@naver.com")
    private String email;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
}
