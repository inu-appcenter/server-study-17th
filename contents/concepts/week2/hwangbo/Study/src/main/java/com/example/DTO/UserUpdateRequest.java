package com.example.DTO;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
}
