package com.example.ticketing.show.dto.req;

import com.example.ticketing.show.ShowState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowUpdateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Schema(example = "제목")
    private String showTitle;

    @NotBlank(message = "사진을 첨부해주세요.")
    @Schema(example = "url")
    private String posterImage;

    @NotNull(message = "티켓팅 시작일을 입력해주세요.")
    private LocalDateTime ticketingDate;

    @NotNull(message = "공연 시작일을 입력해주세요.")
    private LocalDateTime startDate;

    @NotNull(message = "공연 종료일을 입력해주세요.")
    private LocalDateTime endDate;

    @NotNull(message = "공연 상태를 입력해주세요.")
    @Schema(example = "BEFORE_RESERVATION / DURING_RESERVATION / COMPLETED")
    private ShowState showState;
}
