package com.example.gotsaeng_back.post.service;

import com.example.gotsaeng_back.post.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByPostId(Long postId);

    void save(Comment comment);

    Comment findById(Long id);

    void deleteById(Long id);
}
