package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);

    void editPost(Long postId, PostEditDTO postEditDTO);

    void deletePost(Long postId);

    Post createPost(PostCreateDTO postCreateDTO, String token);
}
