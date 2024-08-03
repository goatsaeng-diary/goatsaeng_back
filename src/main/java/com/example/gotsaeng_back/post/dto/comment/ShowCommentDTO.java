package com.example.gotsaeng_back.post.dto.comment;

import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.post.entity.Post;
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
