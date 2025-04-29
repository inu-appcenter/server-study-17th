package com.blog.appcenter_server_week2.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String location;

    private String profileUrl;

}
