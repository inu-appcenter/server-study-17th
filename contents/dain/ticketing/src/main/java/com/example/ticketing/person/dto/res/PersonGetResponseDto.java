package com.example.ticketing.person.dto.res;

import com.example.ticketing.person.Grade;
import com.example.ticketing.person.Person;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PersonGetResponseDto {
    private final String loginId;
    private final String userName;
    private final String email;
    private final Grade grade;
    private final String address;
    private final LocalDateTime createdDate;

    @Builder
    public PersonGetResponseDto(String loginId, String userName, String email, Grade grade, String address, LocalDateTime createdDate) {
        this.loginId = loginId;
        this.userName = userName;
        this.email = email;
        this.grade = grade;
        this.address = address;
        this.createdDate = createdDate;
    }

    public static PersonGetResponseDto from(Person person) {
        return PersonGetResponseDto.builder()
                .loginId(person.getLoginId())
                .userName(person.getUserName())
                .email(person.getEmail())
                .grade(person.getGrade())
                .address(person.getAddress())
                .createdDate(person.getCreatedDate())
                .build();
    }
}

