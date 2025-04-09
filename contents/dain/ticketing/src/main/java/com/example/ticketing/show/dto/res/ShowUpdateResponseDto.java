package com.example.ticketing.show.dto.res;

import com.example.ticketing.show.Show;
import com.example.ticketing.show.dto.req.ShowUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShowUpdateResponseDto {
    private final Long id;

    @Builder
    public ShowUpdateResponseDto(Long id) {
        this.id = id;
    }

    public static ShowUpdateResponseDto from(Show show) {
        return ShowUpdateResponseDto.builder()
                .id(show.getId())
                .build();
    }
}
