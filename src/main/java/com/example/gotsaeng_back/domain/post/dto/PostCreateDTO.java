package com.example.gotsaeng_back.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCreateDTO {
    private String title;

    private String content;
}
