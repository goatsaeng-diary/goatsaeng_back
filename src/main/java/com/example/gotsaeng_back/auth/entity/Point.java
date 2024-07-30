package com.example.gotsaeng_back.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="points")
@Getter
@Setter
@NoArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "point_history")
    private String pointHistory;

    @Column(name = "get_date")
    private LocalDateTime getDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}