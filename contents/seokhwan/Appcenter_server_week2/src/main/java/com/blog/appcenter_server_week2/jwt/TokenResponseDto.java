package com.blog.appcenter_server_week2.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponseDto {
    private final String token;

    @Builder
    public TokenResponseDto(String token) {
        this.token = token;
    }

}
