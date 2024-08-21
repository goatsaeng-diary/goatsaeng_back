package com.example.gotsaeng_back.domain.post.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostDetailDTO {
    private String nickname;

    private String title;

    private String content;

    private List<String> files;

    private boolean like;

    private String userImage;

    private LocalDateTime createDate;

    private Long commentCount;

    private Long likeCount;

    private Long viewCount;
}
