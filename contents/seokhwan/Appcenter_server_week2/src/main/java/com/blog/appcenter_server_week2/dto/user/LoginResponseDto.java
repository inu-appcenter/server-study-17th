package com.blog.appcenter_server_week2.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String loginId;

    @Builder
    public LoginResponseDto(String loginId) {
        this.loginId = loginId;
    }
}
