package com.example.gotsaeng_back.post.service;

import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.post.dto.CreateCommentDTO;
import com.example.gotsaeng_back.post.dto.UpdateCommentDTO;
import com.example.gotsaeng_back.post.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByPostId(Long postId);

    void save(CreateCommentDTO commentDTO, String token, Long postId);

    Comment findById(Long id);

    ApiResponse deleteById(Long id, String token);

    ApiResponse updateById(Long commentId, String token, UpdateCommentDTO commentDTO);
}
