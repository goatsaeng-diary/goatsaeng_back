package com.example.gotsaeng_back.post.service;

import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.post.dto.reply.CreateReplyDTO;
import com.example.gotsaeng_back.post.dto.reply.ShowReplyDTO;
import com.example.gotsaeng_back.post.dto.reply.UpdateReplyDTO;

import java.util.List;

public interface ReplyService {
    void save(CreateReplyDTO replyDTO, String token, Long postId);

    ApiResponse deleteById(Long replyId, String token);

    ApiResponse updateById(Long replyId, String token, UpdateReplyDTO replyDTO);

    ApiResponse<List<ShowReplyDTO>> findByCommentId(Long commentId);
}
