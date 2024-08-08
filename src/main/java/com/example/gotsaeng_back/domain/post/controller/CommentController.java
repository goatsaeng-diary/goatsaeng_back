package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.service.CommentService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
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
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create/{postId}")
    public CustomResponse<Void> createComment(@PathVariable Long postId, @RequestBody CreateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {
        try {
            commentService.save(commentDTO, token, postId);
            return new CustomResponse<>(HttpStatus.OK, "댓글 작성 성공", null);
        } catch (ApiException e) {
            return new CustomResponse<>(e.getException().getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 작성 실패: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public CustomResponse<Void> deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token) {
        try {
            commentService.deleteById(commentId, token);
            return new CustomResponse<>(HttpStatus.OK, "댓글 삭제 성공", null);
        } catch (ApiException e) {
            return new CustomResponse<>(e.getException().getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 삭제 실패: " + e.getMessage(), null);
        }
    }

    @PostMapping("/update/{commentId}")
    public CustomResponse<Void> updateComment(@PathVariable("commentId") Long commentId, @RequestBody UpdateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {
        try {
            commentService.updateById(commentId, token, commentDTO);
            return new CustomResponse<>(HttpStatus.OK, "댓글 수정 성공", null);
        } catch (ApiException e) {
            return new CustomResponse<>(e.getException().getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 수정 실패: " + e.getMessage(), null);
        }
    }

    @GetMapping("/show/{postId}")
    public CustomResponse<List<ShowCommentDTO>> showComment(@PathVariable("postId") Long postId) {
        try {
            List<ShowCommentDTO> comments = commentService.findByPostId(postId);
            return new CustomResponse<>(HttpStatus.OK, "댓글 리스트 조회 성공", comments);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 리스트 조회 실패: " + e.getMessage(), null);
        }
    }
}
