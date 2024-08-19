package com.example.gotsaeng_back.domain.post.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.entity.Like;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.domain.post.repository.LikeRepository;
import com.example.gotsaeng_back.domain.post.service.LikeService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void saveLike(Like like) {
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void addLike(Long postId,String token) {
        Post post = postService.getByPostId(postId);
        User user = userService.findById(jwtUtil.getUserIdFromToken(token));
        Like like = new Like();
        like.setLikeUser(user);
        like.setPost(post);
        saveLike(like);
    }

    @Override
    @Transactional
    public void removeLike(Long postId,String token) {
        Post post = postService.getByPostId(postId);
        User user = userService.findById(jwtUtil.getUserIdFromToken(token));
        likeRepository.deleteLikeByLikeUserAndPost(user,post);
    }
}
