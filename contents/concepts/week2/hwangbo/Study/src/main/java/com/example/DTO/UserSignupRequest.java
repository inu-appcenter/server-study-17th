package com.example.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignupRequest {

    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수로 입력해야 합니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식은 010-1234-5678")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수로 입력해야 합니다.")
    private String address;

    public UserSignupRequest(String email, String password, String name, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
