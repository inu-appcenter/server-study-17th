package com.example.ticketing.person.dto.req;

import lombok.Getter;

@Getter
public class PersonUpdateRequestDto {
    private String userName;

    private String email;

    private String address;
}
