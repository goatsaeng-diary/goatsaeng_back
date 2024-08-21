package com.example.gotsaeng_back.domain.auth.entity;

import com.example.gotsaeng_back.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Histories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Column(name = "view_day", nullable = false)
    private LocalDate viewDay;

    @PrePersist
    public void prePersist() {
        this.viewDay = LocalDate.now();
    }
}
