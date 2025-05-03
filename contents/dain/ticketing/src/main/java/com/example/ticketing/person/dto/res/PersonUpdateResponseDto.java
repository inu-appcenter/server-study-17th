package com.example.ticketing.person.dto.res;

import com.example.ticketing.person.Grade;
import com.example.ticketing.person.Person;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PersonUpdateResponseDto {
    private final Long id;
    private final String personName;
    private final String email;
    private final Grade grade;
    private final String address;
    private final LocalDateTime createdDate;

    @Builder
    private PersonUpdateResponseDto(Long id, String personName, String email, Grade grade, String address, LocalDateTime createdDate) {
        this.id = id;
        this.personName = personName;
        this.email = email;
        this.grade = grade;
        this.address = address;
        this.createdDate = createdDate;
    }

    public static PersonUpdateResponseDto from(Person person) {
        return PersonUpdateResponseDto.builder()
                .id(person.getId())
                .personName(person.getUserName())
                .email(person.getEmail())
                .grade(person.getGrade())
                .address(person.getAddress())
                .createdDate(person.getCreatedDate())
                .build();
    }
}

