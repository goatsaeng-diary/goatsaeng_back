package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.dto.reply.CreateReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.ShowReplyDTO;
import com.example.gotsaeng_back.domain.post.dto.reply.UpdateReplyDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.domain.post.service.ReplyService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    //대댓글 추가
    @PostMapping("/create/{commentId}")
    public ApiResponse<Post> createComment(@PathVariable("commentId") Long commentId, @RequestBody CreateReplyDTO replyDTO, @RequestHeader("Authorization") String token) {
        replyService.save(replyDTO, token, commentId);
        return new ApiResponse<>(true, "대댓글 작성 성공");
    }

    //대댓글 삭제
    @DeleteMapping("/delete/{replyId}")
    public ApiResponse deleteComment(@PathVariable("replyId") Long replyId, @RequestHeader("Authorization") String token) {
        return replyService.deleteById(replyId, token);
    }

    //대댓글 수정
    @PostMapping("/update/{replyId}")
    public ApiResponse updateComment(@PathVariable("replyId") Long replyId, @RequestBody UpdateReplyDTO replyDTO, @RequestHeader("Authorization") String token){
        return replyService.updateById(replyId, token, replyDTO);
    }

    // 댓글별 대댓글 리스트 불러오기
    @GetMapping("/show/{commentId}")
    public ApiResponse<List<ShowReplyDTO>> showComment(@PathVariable("commentId") Long commentId) {
        System.out.println("리스트 불러오기");
        return replyService.findByCommentId(commentId);
    }
}
