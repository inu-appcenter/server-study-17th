package com.example.DTO;

import lombok.Getter;
import org.apache.catalina.User;

@Getter
public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;

    public UserSignupRequest(String email, String password, String name, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
