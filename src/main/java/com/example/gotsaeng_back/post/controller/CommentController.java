package com.example.gotsaeng_back.post.controller;

import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import com.example.gotsaeng_back.post.dto.CreateCommentDTO;
import com.example.gotsaeng_back.post.dto.DeleteCommentDTO;
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

    @PostMapping("/create/{postId}")
    public ApiResponse<Post> createComment(@PathVariable Long postId, @RequestBody CreateCommentDTO commentDTO, @RequestHeader("Authorization") String token) {


        // JWT 토큰에서 사용자 이름 추출
        String username = jwtUtil.getUsernameFromAccessToken(token);

        // 사용자 정보 조회
        User user = userService.findByUsername(username);

        // 댓글 작성
        Post post = postService.getByPostId(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());
        commentService.save(comment);

        return new ApiResponse<>(true, "댓글 작성 성공");
    }

    @DeleteMapping("/delete/{commentId}")
    public ApiResponse deleteComment(@RequestHeader("Authorization") String token, @PathVariable("commentId") Long commentId) {
        try {
            // JWT 토큰에서 사용자 이름 추출
            String username = jwtUtil.getUsernameFromAccessToken(token);

            // 사용자 정보 조회
            User user = userService.findByUsername(username);
            Long userId = user.getUserId();

            Comment comment = commentService.findById(commentId);
            Long commentUserId = comment.getUser().getUserId();

            // 사용자와 댓글 작성자가 일치하는지 확인
            if (userId.equals(commentUserId)) {
                commentService.deleteById(commentId);
                return new ApiResponse<>(true, "댓글 삭제 성공");
            } else {
                return new ApiResponse<>(false, "댓글 삭제 실패: 권한이 없습니다.");
            }
        } catch (Exception e) {
            return new ApiResponse<>(false, "댓글 삭제 실패: " + e.getMessage());
        }
    }



}
