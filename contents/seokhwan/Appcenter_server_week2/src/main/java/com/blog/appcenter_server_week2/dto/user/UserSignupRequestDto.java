package com.blog.appcenter_server_week2.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "아이디를 입력하세요")
    @Schema(description = "로그인 아이디", example = "qwre1234")
    private String loginId;

    @NotBlank(message = "이름을 입력하세요")
    @Schema(description = "이름", example = "seokhwan")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Schema(description = "비밀번호", example = "!qwer1234")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요")
    @Schema(description = "닉네임")
    private String nickname;

    @NotBlank(message = "주소를 입력하세요")
    @Schema(description = "주소")
    private String location;

    private String profileUrl;

}
