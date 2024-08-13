package com.example.gotsaeng_back.domain.post.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PostListDTO {
    private List<PostDetailDTO> posts;
}
