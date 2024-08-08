package com.example.gotsaeng_back.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long point;

    @Column(name = "started_date", nullable = false)
    private LocalDateTime startedDate;

    @Column(name = "ended_date", nullable = false)
    private LocalDateTime endedDate;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeCategoryType category;
}
