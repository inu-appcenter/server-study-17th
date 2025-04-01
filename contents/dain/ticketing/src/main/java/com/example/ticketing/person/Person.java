package com.example.ticketing.person;

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
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login_id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String user_name;

    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private LocalDate created_date;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Person(String login_id, String password, String user_name, String email, String address, Grade grade, LocalDate created_date) {
        this.login_id = login_id;
        this.password = password;
        this.user_name = user_name;
        this.email = email;
        this.address = address;
        this.grade = grade;
        this.created_date = created_date;
    }
}
