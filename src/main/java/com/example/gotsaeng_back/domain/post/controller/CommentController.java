package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.service.CommentService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create/{postId}")
    public CustomResponse<Void> createComment(@PathVariable Long postId, @RequestBody CreateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {
        commentService.save(commentDTO, token, postId);
        return new CustomResponse<>(HttpStatus.OK, "댓글 작성 성공", null);
    }

    @DeleteMapping("/delete/{commentId}")
    public CustomResponse<Void> deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token) {
        commentService.deleteById(commentId, token);
        return new CustomResponse<>(HttpStatus.OK, "댓글 삭제 성공", null);
    }

    @PostMapping("/update/{commentId}")
    public CustomResponse<Void> updateComment(@PathVariable("commentId") Long commentId, @RequestBody UpdateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {
        commentService.updateById(commentId, token, commentDTO);
        return new CustomResponse<>(HttpStatus.OK, "댓글 수정 성공", null);
    }

    @GetMapping("/show/{postId}")
    public CustomResponse<List<ShowCommentDTO>> showComment(@PathVariable("postId") Long postId) {
        List<ShowCommentDTO> comments = commentService.findByPostId(postId);
        return new CustomResponse<>(HttpStatus.OK, "댓글 리스트 조회 성공", comments);
    }
}
