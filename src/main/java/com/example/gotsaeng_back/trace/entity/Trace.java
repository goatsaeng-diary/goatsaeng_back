package com.example.gotsaeng_back.trace.entity;

import com.example.gotsaeng_back.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trace_id", nullable = false)
    private Long traceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private TraceCategoryType category;
}
