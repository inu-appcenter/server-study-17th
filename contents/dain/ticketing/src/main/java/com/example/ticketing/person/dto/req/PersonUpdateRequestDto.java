package com.example.ticketing.person.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PersonUpdateRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
}
