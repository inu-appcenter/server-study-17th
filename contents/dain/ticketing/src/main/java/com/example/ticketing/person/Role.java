package com.example.ticketing.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN, //ROLE 이미 있음 prefix
    ROLE_USER
}