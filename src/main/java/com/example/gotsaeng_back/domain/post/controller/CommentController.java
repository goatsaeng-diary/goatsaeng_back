package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.service.CommentService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
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

    // 게시글별 댓글 리스트 불러오기
    @GetMapping("/show/{postId}")
    public ApiResponse<List<ShowCommentDTO>> showComment(@PathVariable("postId") Long postId) {
        System.out.println("리스트 불러오기");
        return commentService.findByPostId(postId);
    }
}
