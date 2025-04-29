package com.blog.appcenter_server_week2.dto.user;

import com.blog.appcenter_server_week2.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignupResponseDto {

    private final String loginId;

    @Builder
    private UserSignupResponseDto(String loginId) {
        this.loginId = loginId;
    }

    public static UserSignupResponseDto from(User user) {
        return UserSignupResponseDto.builder()
                .loginId(user.getLoginId())
                .build();
    }

}
