package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.entity.Post;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);
}
