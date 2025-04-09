package com.example.ticketing.person;

import com.example.ticketing.person.dto.req.PersonUpdateRequestDto;
import com.example.ticketing.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String userName;

    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder
    private Person(String loginId, String password, String userName, String email, String address, Grade grade, LocalDateTime createdDate) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.grade = grade;
        this.createdDate = createdDate;
    }

    //현재 인스턴스의 필드를 직접 수정(반환x)
    public void update(PersonUpdateRequestDto dto) {
        if (dto.getUserName() != null) {
            this.userName = dto.getUserName();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        }
    }