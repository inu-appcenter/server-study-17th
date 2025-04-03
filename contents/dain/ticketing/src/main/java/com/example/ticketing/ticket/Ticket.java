package com.example.ticketing.ticket;

import com.example.ticketing.person.Person;
import com.example.ticketing.show.Show;
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
    private String ticket_number;

    @Column(nullable = false)
    private String ticket_seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketState ticket_state;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @Builder
    private Ticket(String ticket_number, String ticket_seat, TicketState ticket_state, Person person, Show show) {
        this.ticket_number = ticket_number;
        this.ticket_seat = ticket_seat;
        this.ticket_state = ticket_state;
        this.person = person;
        this.show = show;
    }
}
