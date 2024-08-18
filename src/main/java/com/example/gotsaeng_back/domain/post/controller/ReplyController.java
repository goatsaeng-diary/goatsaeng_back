package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.dto.reply.CreateReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.ShowReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.UpdateReplyDTO;
import com.example.gotsaeng_back.domain.post.service.ReplyService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    // 대댓글 추가
    @PostMapping("/create/{commentId}")
    public CustomResponse<Void> createReply(@PathVariable("commentId") Long commentId, @RequestBody CreateReplyDTO replyDTO, @RequestHeader("Authorization") String token) {
        replyService.save(replyDTO, token, commentId);
        return new CustomResponse<>(HttpStatus.OK, "대댓글을 작성을 완료했습니다.", null);
    }

    // 대댓글 삭제
    @DeleteMapping("/delete/{replyId}")
    public CustomResponse<Void> deleteReply(@PathVariable("replyId") Long replyId, @RequestHeader("Authorization") String token) {
        replyService.deleteById(replyId, token);
        return new CustomResponse<>(HttpStatus.OK, "대댓글을 삭제했습니다.", null);
    }

    // 대댓글 수정
    @PostMapping("/update/{replyId}")
    public CustomResponse<Void> updateReply(@PathVariable("replyId") Long replyId, @RequestBody UpdateReplyDTO replyDTO, @RequestHeader("Authorization") String token) {
        replyService.updateById(replyId, token, replyDTO);
        return new CustomResponse<>(HttpStatus.OK, "대댓글을 수정했습니다.", null);
    }

    // 댓글별 대댓글 리스트 불러오기
    @GetMapping("/show/{commentId}")
    public CustomResponse<List<ShowReplyDTO>> showReply(@PathVariable("commentId") Long commentId) {
        List<ShowReplyDTO> replies = replyService.findByCommentId(commentId);
        return new CustomResponse<>(HttpStatus.OK, "대댓글 리스트를 조회했습니다.", replies);
    }
}
