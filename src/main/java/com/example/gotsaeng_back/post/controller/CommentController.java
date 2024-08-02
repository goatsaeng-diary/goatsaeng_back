package com.example.gotsaeng_back.post.controller;

import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import com.example.gotsaeng_back.post.dto.CreateCommentDTO;
import com.example.gotsaeng_back.post.dto.DeleteCommentDTO;
import com.example.gotsaeng_back.post.dto.UpdateCommentDTO;
import com.example.gotsaeng_back.post.entity.Comment;
import com.example.gotsaeng_back.post.entity.Post;
import com.example.gotsaeng_back.post.service.CommentService;
import com.example.gotsaeng_back.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //댓글 추가
    @PostMapping("/create/{postId}")
    public ApiResponse<Post> createComment(@PathVariable Long postId, @RequestBody CreateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {
        commentService.save(commentDTO, token, postId);
        return new ApiResponse<>(true, "댓글 작성 성공");
    }

    //댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ApiResponse deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token) {
        return commentService.deleteById(commentId, token);
    }

    //댓글 수정
    @PostMapping("/update/{commentId}")
    public ApiResponse updateComment(@PathVariable("commentId") Long commentId, @RequestBody UpdateCommentDTO commentDTO, @RequestHeader("Authorization") String token){
        return commentService.updateById(commentId, token, commentDTO);
    }
}
