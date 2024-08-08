package com.example.gotsaeng_back.domain.post.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ShowCommentDTO {
    private Long commentId;
    private Long postId;
    private String content;
    private LocalDateTime createdDate;
    private String username;
}
