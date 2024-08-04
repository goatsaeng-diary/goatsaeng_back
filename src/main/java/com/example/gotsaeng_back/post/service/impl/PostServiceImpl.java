package com.example.gotsaeng_back.post.service.impl;

import com.example.gotsaeng_back.global.exception.handler.GlobalExceptionHandler;
import com.example.gotsaeng_back.post.entity.Post;
import com.example.gotsaeng_back.post.repository.PostRepository;
import com.example.gotsaeng_back.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;


    @Transactional
    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post getByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(()-> new RuntimeException("게시물이 없습니다."));
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        postRepository.findById(postId).orElseThrow(()-> new RuntimeException("게시물이 없습니다."));
        postRepository.deleteById(postId);
    }
}
