package com.example.ticketing.person.dto.res;

import com.example.ticketing.person.Person;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonSignupResponseDto {
    private final Long id;
    private final String loginId;

    @Builder
    private PersonSignupResponseDto(Long id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }

    public static PersonSignupResponseDto from(Person person) {
        return PersonSignupResponseDto.builder()
                .id(person.getId())
                .loginId(person.getLoginId())
                .build();
    }
}
