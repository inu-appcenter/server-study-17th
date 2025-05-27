package com.example.ticketing.ticket.dto.req;

import com.example.ticketing.ticket.TicketState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TicketUpdateRequestDto {
    @NotBlank(message = "좌석 정보를 입력해주세요.")
    private String ticketSeat;

    @NotNull(message = "티켓 상태를 입력해주세요.")
    private TicketState ticketState;
}
