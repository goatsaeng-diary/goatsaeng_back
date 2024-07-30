package com.example.gotsaeng_back.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "replys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private Long replyId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    private Comment commentId;
}
