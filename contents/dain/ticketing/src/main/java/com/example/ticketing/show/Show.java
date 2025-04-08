package com.example.ticketing.show;

import com.example.ticketing.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String show_title;

    @Column(nullable = false)
    private String poster_image;

    private String detailed_image;

    private LocalDate ticketing_date;

    private LocalDate start_date;

    private LocalDate end_date;

    private String show_rate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowState show_state;

    private String cast;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Show(String show_title, String poster_image, String detailed_image, LocalDate ticketing_date, LocalDate start_date, LocalDate end_date, String show_rate, ShowState show_state, String cast) {
        this.show_title = show_title;
        this.poster_image = poster_image;
        this.detailed_image = detailed_image;
        this.ticketing_date = ticketing_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.show_rate = show_rate;
        this.cast = cast;
    }

}
