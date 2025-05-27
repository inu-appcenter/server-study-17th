package com.example.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Schema(description = "회원가입 요청 모델")
@Getter
public class UserSignupRequest {

    // 모든 필드가 필수로 입력된다면 @NotBlank를 한 곳에서 할 수 있는 방법이 있을까?

    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    @Schema(description = "사용자 비밀번호", example = "Password123!")
    private String password;

    @NotBlank(message = "비밀번호 재입력은 필수로 입력해야 합니다.")
    @Schema(description = "사용자 비밀번호 재입력", example = "Password123!")
    private String passwordCheck;

    @NotBlank(message = "이름은 필수로 입력해야 합니다.")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @NotBlank(message = "전화번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식은 010-1234-5678")
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수로 입력해야 합니다.")
    @Schema(description = "주소", example = "서울특별시 강남구")
    private String address;

    public UserSignupRequest(String email, String password, String passwordCheck, String name, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
