package com.example.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    public User(String email, String password, String name, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void UpdateInfo(String password, String name, String phoneNumber, String address) {
        if (password != null){
            this.password = password;
        }
        if (name != null) {
            this.name = name;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (address != null) {
            this.address = address;
        }
    }
}
