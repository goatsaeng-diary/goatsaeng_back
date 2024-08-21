package com.example.gotsaeng_back.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long pointId;

    @Column(name = "point_history")
    private String pointHistory;

    @Column(name = "get_date", nullable = false)
    private LocalDateTime getDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}