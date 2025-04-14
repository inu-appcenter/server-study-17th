package com.example.ticketing.show.dto.res;

import com.example.ticketing.show.Show;
import com.example.ticketing.show.ShowState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowGetResponseDto {
    private final Long id;
    private final String showTitle;
    private final String posterImage;
    private final String detailedImage;
    private final LocalDateTime ticketingDate;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String showRate;
    private final ShowState showState;
    private final String casts;

    @Builder
    public ShowGetResponseDto(Long id, String showTitle, String posterImage, String detailedImage,
                              LocalDateTime ticketingDate, LocalDateTime startDate, LocalDateTime endDate,
                              String showRate, ShowState showState, String casts) {
        this.id = id;
        this.showTitle = showTitle;
        this.posterImage = posterImage;
        this.detailedImage = detailedImage;
        this.ticketingDate = ticketingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showRate = showRate;
        this.showState = showState;
        this.casts = casts;
    }

    public static ShowGetResponseDto from(Show show) {
        return ShowGetResponseDto.builder()
                .id(show.getId())
                .showTitle(show.getShowTitle())
                .posterImage(show.getPosterImage())
                .detailedImage(show.getDetailedImage())
                .ticketingDate(show.getTicketingDate())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .showRate(show.getShowRate())
                .showState(show.getShowState())
                .casts(show.getCasts())
                .build();
    }
}
