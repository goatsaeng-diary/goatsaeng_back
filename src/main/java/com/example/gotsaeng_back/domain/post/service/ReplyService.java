package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.reply.CreateReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.ShowReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.UpdateReplyDTO;

import java.util.List;

public interface ReplyService {
    void save(CreateReplyDTO replyDTO, String token, Long postId);

    void deleteById(Long replyId, String token);

    void updateById(Long replyId, String token, UpdateReplyDTO replyDTO);

    List<ShowReplyDTO> findByCommentId(Long commentId);
}
