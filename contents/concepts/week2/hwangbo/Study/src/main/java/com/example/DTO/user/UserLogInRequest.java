package com.example.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "로그인 요청 모델")
@Getter
public class UserLogInRequest {

    @Email
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    @Schema(description = "사용자 비밀번호", example = "Password123!")
    private String password;
}
