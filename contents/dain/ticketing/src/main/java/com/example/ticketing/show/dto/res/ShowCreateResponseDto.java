package com.example.ticketing.show.dto.res;

import com.example.ticketing.show.Show;
import com.example.ticketing.show.ShowState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowCreateResponseDto {
    private final Long id;
    private final String showTitle;
    private final String posterImage;
    private final LocalDateTime ticketingDate;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final ShowState showState;


    @Builder
    private ShowCreateResponseDto(Long id, String showTitle, String posterImage,
                                 LocalDateTime ticketingDate, LocalDateTime startDate, LocalDateTime endDate,
                                 ShowState showState) {
        this.id = id;
        this.showTitle = showTitle;
        this.posterImage = posterImage;
        this.ticketingDate = ticketingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showState = showState;
    }

    public static ShowCreateResponseDto from(Show show) {
        return ShowCreateResponseDto.builder()
                .id(show.getId())
                .showTitle(show.getShowTitle())
                .posterImage(show.getPosterImage())
                .ticketingDate(show.getTicketingDate())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .showState(show.getShowState())
                .build();
    }
}
