package com.example.ticketing.person.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PersonSignupRequestDto {

    @NotBlank(message = "ID를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
