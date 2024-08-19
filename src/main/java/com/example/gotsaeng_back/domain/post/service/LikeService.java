package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.entity.Like;

public interface LikeService {
    void saveLike(Like like);
    void addLike(Long postId,String token);
    void removeLike(Long postId,String token);

}
