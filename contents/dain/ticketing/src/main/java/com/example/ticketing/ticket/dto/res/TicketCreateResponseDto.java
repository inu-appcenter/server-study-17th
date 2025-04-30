package com.example.ticketing.ticket.dto.res;

import com.example.ticketing.ticket.Ticket;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketCreateResponseDto {
    private final Long id;
    private final String ticketNumber;

    @Builder
    private TicketCreateResponseDto(Long id, String ticketNumber) {
        this.id = id;
        this.ticketNumber = ticketNumber;
    }

    public static TicketCreateResponseDto from(Ticket ticket) {
        return TicketCreateResponseDto.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .build();
    }
}
