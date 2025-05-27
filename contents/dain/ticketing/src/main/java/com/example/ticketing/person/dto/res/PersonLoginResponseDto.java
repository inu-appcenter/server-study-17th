package com.example.ticketing.person.dto.res;

import com.example.ticketing.person.Person;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonLoginResponseDto {
    private final Long userId;
    private final String name;
    private final String accessToken;   // JWT 토큰
    private final String refreshToken;

    @Builder
    private PersonLoginResponseDto(Long userId, String name, String accessToken, String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static PersonLoginResponseDto from(Person person, String token) {
        return PersonLoginResponseDto.builder()
                .userId(person.getId())
                .name(person.getUserName())
                .accessToken(token)
                .refreshToken(token)
                .build();
    }
}
