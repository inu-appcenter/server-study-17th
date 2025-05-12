package com.blog.appcenter_server_week2.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "아이디를 입력하세요")
    private String loginId;

    @NotBlank(message = "이름을 입력하세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요")
    private String nickname;

    @NotBlank(message = "주소를 입력하세요")
    private String location;

    private String profileUrl;

}
