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
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void save(CreateReplyDTO replyDTO, String token, Long commentId) {
        // JWT 토큰에서 사용자 이름 추출
        String username = jwtUtil.getUserNameFromToken(token);

        // 사용자 정보 조회
        User user = userService.findByUsername(username);

        // 댓글 작성
        Comment comment = commentService.findById(commentId);
        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setUser(user);
        reply.setContent(replyDTO.getContent());
        replyRepository.save(reply);
    }

    @Override
    public ApiResponse deleteById(Long replyId, String token) {
        try {
            // JWT 토큰에서 사용자 이름 추출
            String username = jwtUtil.getUserNameFromToken(token);

            // 사용자 정보 조회
            User user = userService.findByUsername(username);
            Long userId = user.getUserId();

            Reply reply = findById(replyId);
            if (reply == null) {
                return new ApiResponse<>(false, "대댓글을 찾을 수 없습니다.");
            }

            Long replyUserId = reply.getUser().getUserId();

            // 사용자와 댓글 작성자가 일치하는지 확인
            if (!userId.equals(replyUserId)) {
                return new ApiResponse<>(false, "대댓글 삭제 권한이 없습니다.");
            }

            replyRepository.deleteById(replyId);
            return new ApiResponse<>(true, "대댓글 삭제 성공");
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateById(Long replyId, String token, UpdateReplyDTO replyDTO) {
        try {
            // JWT 토큰에서 사용자 이름 추출
            String username = jwtUtil.getUserNameFromToken(token);

            // 사용자 정보 조회
            User user = userService.findByUsername(username);
            Long userId = user.getUserId();

            Reply reply = findById(replyId);
            if (reply == null) {
                return new ApiResponse<>(false, "대댓글을 찾을 수 없습니다.");
            }

            Long replyUserId = reply.getUser().getUserId();

            // 사용자와 댓글 작성자가 일치하는지 확인
            if (!userId.equals(replyUserId)) {
                return new ApiResponse<>(false, "대댓글 수정 권한이 없습니다.");
            }

            // 댓글 수정
            reply.setContent(replyDTO.getContent());
            reply.setCreatedDate(LocalDateTime.now());
            replyRepository.save(reply);

            return new ApiResponse<>(true, "대댓글 수정 성공");
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<ShowReplyDTO>> findByCommentId(Long commentId) {
        try {
            List<Reply> replies = replyRepository.findByCommentCommentId(commentId);
            if (replies.isEmpty()) {
                return new ApiResponse<>(false, "대댓글이 없습니다.", null);
            }
            List<ShowReplyDTO> replyDtoList = replies.stream()
                    .map(reply -> new ShowReplyDTO(
                            reply.getReplyId(),
                            reply.getComment().getCommentId(),
                            reply.getContent(),
                            reply.getCreatedDate(),
                            reply.getUser().getUsername()
                    ))
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "대댓글 리스트 조회 성공", replyDtoList);
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage(), null);
        }
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId).orElse(null);
    }
}
