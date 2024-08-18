package com.example.gotsaeng_back.domain.post.dto.post;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class PostDetailDTO {
    private String nickname;

    private String title;

    private String content;

    private List<String> files;
}
