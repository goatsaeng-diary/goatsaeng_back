package com.example.gotsaeng_back.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false)
    private Long badgeId;

    @Column(nullable = false)
    private String name;

    @Column(name = "tier", nullable = false)
    @Enumerated(EnumType.STRING)
    private TierType tier;

    @Column(nullable = false)
    private String content;
}
