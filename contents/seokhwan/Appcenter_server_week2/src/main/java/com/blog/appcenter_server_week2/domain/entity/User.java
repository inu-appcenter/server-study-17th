package com.blog.appcenter_server_week2.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigInteger;
import java.net.ProtocolFamily;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;


    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String location;

    @Column
    private String profile_url;

    @Column
    @ColumnDefault("36.5")
    private Double manner;

    @Builder
    private User(String loginId, String username, String password, String nickname, String location, String profile_url, Double manner) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.location = location;
        this.profile_url = profile_url;
        this.manner = manner;
    }

    public User update(String loginId, String username, String password, String nickname, String location, String profile_url) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.location = location;
        this.profile_url = profile_url;
        return this;
    }

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
