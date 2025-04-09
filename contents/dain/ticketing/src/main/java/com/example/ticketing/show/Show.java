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

    private String posterImage;

    private String detailedImage;

    private LocalDateTime ticketingDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String showRate;

    @Enumerated(EnumType.STRING)
    private ShowState showState;

    private String casts;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Show(String showTitle, String posterImage, String detailedImage,
                 LocalDateTime ticketingDate, LocalDateTime startDate, LocalDateTime endDate,
                 String showRate, ShowState showState, String casts) {
        this.showTitle = showTitle;
        this.posterImage = posterImage;
        this.detailedImage = detailedImage;
        this.ticketingDate = ticketingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showRate = showRate;
        this.showState = showState;
        this.casts = casts;
    }

    public Show update(ShowUpdateRequestDto dto) {
        this.showTitle = dto.getShowTitle() != null ? dto.getShowTitle() : this.showTitle;
        this.posterImage = dto.getPosterImage() != null ? dto.getPosterImage() : this.posterImage;
        this.detailedImage = dto.getDetailedImage() != null ? dto.getDetailedImage() : this.detailedImage;
        this.ticketingDate = dto.getTicketingDate() != null ? dto.getTicketingDate() : this.ticketingDate;
        this.startDate = dto.getStartDate() != null ? dto.getStartDate() : this.startDate;
        this.endDate = dto.getEndDate() != null ? dto.getEndDate() : this.endDate;
        this.showRate = dto.getShowRate() != null ? dto.getShowRate() : this.showRate;
        this.showState = dto.getShowState() != null ? dto.getShowState() : this.showState;
        this.casts = dto.getCasts() != null ? dto.getCasts() : this.casts;
        return this;
    }

}
