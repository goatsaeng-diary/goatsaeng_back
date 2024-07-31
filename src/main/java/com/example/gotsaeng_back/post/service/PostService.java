package com.example.gotsaeng_back.post.service;

import com.example.gotsaeng_back.post.entity.Post;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);
}
