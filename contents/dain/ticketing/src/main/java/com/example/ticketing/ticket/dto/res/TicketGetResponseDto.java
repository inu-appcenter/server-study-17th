package com.example.ticketing.ticket.dto.res;

import com.example.ticketing.ticket.Ticket;
import com.example.ticketing.ticket.TicketState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketGetResponseDto {
    private final Long id;
    private final String ticketNumber;
    private final String ticketSeat;
    private final TicketState ticketState;

    @Builder
    private TicketGetResponseDto(Long id, String ticketNumber, String ticketSeat, TicketState ticketState) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.ticketSeat = ticketSeat;
        this.ticketState = ticketState;
    }

    public static TicketGetResponseDto from(Ticket ticket) {
        return TicketGetResponseDto.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .ticketSeat(ticket.getTicketSeat())
                .ticketState(ticket.getTicketState())
                .build();
    }
}