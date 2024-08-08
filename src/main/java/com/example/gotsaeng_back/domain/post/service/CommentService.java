package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Comment;

import java.util.List;

public interface CommentService {
    ApiResponse<List<ShowCommentDTO>> findByPostId(Long postId);

    void save(CreateCommentDTO commentDTO, String token, Long postId);

    Comment findById(Long id);

    ApiResponse deleteById(Long id, String token);

    ApiResponse updateById(Long commentId, String token, UpdateCommentDTO commentDTO);
}
