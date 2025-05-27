package com.example.ticketing.ticket;

import com.example.ticketing.person.Person;
import com.example.ticketing.show.Show;
import com.example.ticketing.ticket.dto.req.TicketUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private String ticketSeat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketState ticketState;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @Builder
    private Ticket(String ticketNumber, String ticketSeat, TicketState ticketState, Person person, Show show) {
        this.ticketNumber = ticketNumber;
        this.ticketSeat = ticketSeat;
        this.ticketState = ticketState;
        this.person = person;
        this.show = show;
    }

    public void update(TicketUpdateRequestDto dto) {
        this.ticketSeat = dto.getTicketSeat();
        this.ticketState = dto.getTicketState();
    }
}
