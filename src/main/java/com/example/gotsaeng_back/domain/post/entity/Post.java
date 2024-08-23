package com.example.gotsaeng_back.domain.post.entity;

import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @Column(name = "files")
    private List<String> files;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type",nullable = false)
    private RecordType recordType;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<History> histories;
}
