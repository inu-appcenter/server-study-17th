package com.example.ticketing.show;

import com.example.ticketing.show.dto.req.ShowUpdateRequestDto;
import com.example.ticketing.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "shows")
@NoArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String showTitle;

    @Column(nullable = false)
    private String posterImage;

    @Column(nullable = false)
    private LocalDateTime ticketingDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private ShowState showState;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Show(String showTitle, String posterImage, LocalDateTime ticketingDate,
                 LocalDateTime startDate, LocalDateTime endDate, ShowState showState) {
        this.showTitle = showTitle;
        this.posterImage = posterImage;
        this.ticketingDate = ticketingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showState = showState;
    }

    public void update(ShowUpdateRequestDto dto) {
        this.showTitle = dto.getShowTitle();
        this.posterImage = dto.getPosterImage();
        this.ticketingDate = dto.getTicketingDate();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.showState = dto.getShowState();
    }
}
