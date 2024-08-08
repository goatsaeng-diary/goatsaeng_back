package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Comment;

import java.util.List;

public interface CommentService {
    CustomResponse<List<ShowCommentDTO>> findByPostId(Long postId);

    void save(CreateCommentDTO commentDTO, String token, Long postId);

    Comment findById(Long id);

    CustomResponse deleteById(Long id, String token);

    CustomResponse updateById(Long commentId, String token, UpdateCommentDTO commentDTO);
}
