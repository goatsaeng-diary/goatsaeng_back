package com.example.gotsaeng_back.domain.post.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostEditDTO {
    private String title;

    private String content;

    private List<MultipartFile> files;
}