package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.LikeUserDTO;
import com.example.gotsaeng_back.domain.post.entity.Like;
import com.example.gotsaeng_back.domain.post.entity.Post;

import java.util.List;

public interface LikeService {
    void saveLike(Like like);
    void addLike(Post post,String token);
    void removeLike(Post post,String token);
    List<LikeUserDTO> getLikeUsers(Post post);
    boolean isLikePostByUser(Post post, String token);
    Long getLikes(Post post);
    List<Long> getLikePosts(String token);
}
