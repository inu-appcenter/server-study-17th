package com.example.DTO;

import lombok.Getter;
import org.apache.catalina.User;

@Getter
public class UserSignupRequest {
    private String name;
    private String phoneNumber;
    private String address;

    public UserSignupRequest(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
