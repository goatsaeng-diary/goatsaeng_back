package com.example.gotsaeng_back.post.dto;

import com.example.gotsaeng_back.auth.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCreateDTO {
    private String title;

    private User user;

    private String content;
}
