package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.LikeUserDTO;
import com.example.gotsaeng_back.domain.post.entity.Like;

import java.util.List;

public interface LikeService {
    void saveLike(Like like);
    void addLike(Long postId,String token);
    void removeLike(Long postId,String token);
    List<LikeUserDTO> getLikeUsers(Long postId);
    boolean isLikePostByUser(Long postId, String token);
    Long getLikes(Long postId);
    List<Long> getLikePosts(String token);
}
