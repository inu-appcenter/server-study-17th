package com.example.ticketing.show.dto.req;

import com.example.ticketing.show.ShowState;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowUpdateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String showTitle;

    private String posterImage;

    private String detailedImage;

    private LocalDateTime ticketingDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String showRate;

    private ShowState showState;
    
    private String casts;
}
