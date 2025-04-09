package com.example.ticketing.person.dto.res;

import com.example.ticketing.person.Person;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonLoginResponseDto {
    private final Long userId;
    private final String name;

    @Builder
    public PersonLoginResponseDto(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static PersonLoginResponseDto from(Person person) {
        return PersonLoginResponseDto.builder()
                .userId(person.getId())
                .name(person.getUserName())
                .build();
    }
}
