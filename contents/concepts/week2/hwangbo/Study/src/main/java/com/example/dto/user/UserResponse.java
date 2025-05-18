package com.example.dto.user;

import com.example.domain.user.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private String accessToken;


    public UserResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static UserResponse get(User user) {
        return new UserResponse(user.getName());
    }
}
