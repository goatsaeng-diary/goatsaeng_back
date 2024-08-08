package com.example.gotsaeng_back.domain.post.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.dto.reply.CreateReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.ShowReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.UpdateReplyDTO;
import com.example.gotsaeng_back.domain.post.entity.Comment;
import com.example.gotsaeng_back.domain.post.entity.Reply;
import com.example.gotsaeng_back.domain.post.repository.ReplyRepository;
import com.example.gotsaeng_back.domain.post.service.CommentService;
import com.example.gotsaeng_back.domain.post.service.ReplyService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void save(CreateReplyDTO replyDTO, String token, Long commentId) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);
        Comment comment = commentService.findById(commentId);

        if (user == null || comment == null) {
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }

        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setUser(user);
        reply.setContent(replyDTO.getContent());
        replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void deleteById(Long replyId, String token) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Reply reply = findById(replyId);

        if (reply == null) {
            throw new ApiException(ExceptionEnum.REPLY_NOT_FOUND);
        }

        if (!user.getUserId().equals(reply.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.REPLY_DELETE_FORBIDDEN);
        }

        replyRepository.deleteById(replyId);
    }

    @Override
    @Transactional
    public void updateById(Long replyId, String token, UpdateReplyDTO replyDTO) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Reply reply = findById(replyId);

        if (reply == null) {
            throw new ApiException(ExceptionEnum.REPLY_NOT_FOUND);
        }

        if (!user.getUserId().equals(reply.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.REPLY_UPDATE_FORBIDDEN);
        }

        reply.setContent(replyDTO.getContent());
        reply.setCreatedDate(LocalDateTime.now());
        replyRepository.save(reply);
    }

    @Override
    public List<ShowReplyDTO> findByCommentId(Long commentId) {
        List<Reply> replies = replyRepository.findByCommentCommentId(commentId);
        return replies.stream()
                .map(reply -> new ShowReplyDTO(
                        reply.getReplyId(),
                        reply.getComment().getCommentId(),
                        reply.getContent(),
                        reply.getCreatedDate(),
                        reply.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId).orElse(null);
    }
}
