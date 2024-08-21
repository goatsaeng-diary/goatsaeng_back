package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Comment;

import java.util.List;

public interface CommentService {
    List<ShowCommentDTO> findByPostId(Long postId);

    void save(CreateCommentDTO commentDTO, String token, Long postId);

    Comment findById(Long id);

    void deleteById(Long id, String token);

    void updateById(Long commentId, String token, UpdateCommentDTO commentDTO);
}
