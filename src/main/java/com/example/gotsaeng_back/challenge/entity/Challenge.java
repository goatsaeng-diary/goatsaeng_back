package com.example.gotsaeng_back.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long challengeId;

    private String name;

    private String content;

    private Long point;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "ended_date")
    private LocalDateTime endedDate;
}
