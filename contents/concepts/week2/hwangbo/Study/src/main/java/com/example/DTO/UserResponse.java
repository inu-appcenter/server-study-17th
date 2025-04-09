package com.example.DTO;

import com.example.domain.user.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private String name;

    public UserResponse(String name) {
        this.name = name;
    }

    public static UserResponse Get(User user) {
        return new UserResponse(user.getName());
    }
}
