package com.example.gotsaeng_back.post.service.impl;

import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import com.example.gotsaeng_back.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.post.entity.Comment;
import com.example.gotsaeng_back.post.entity.Post;
import com.example.gotsaeng_back.post.repository.CommentRepository;
import com.example.gotsaeng_back.post.service.CommentService;
import com.example.gotsaeng_back.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final JwtUtil jwtUtil;

    //댓글 추가
    public void save(CreateCommentDTO commentDTO, String token, Long postId) {
        // JWT 토큰에서 사용자 이름 추출
        String username = jwtUtil.getUserNameFromToken(token);

        // 사용자 정보 조회
        User user = userService.findByUsername(username);

        // 댓글 작성
        Post post = postService.getByPostId(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    public ApiResponse<List<ShowCommentDTO>> findByPostId(Long postId) {
        try {
            List<Comment> comments = commentRepository.findByPostPostId(postId);
            if (comments.isEmpty()) {
                return new ApiResponse<>(false, "댓글이 없습니다.", null);
            }
            List<ShowCommentDTO> commentDto = comments.stream()
                    .map(comment -> new ShowCommentDTO(
                            comment.getCommentId(),
                            comment.getPost().getPostId(),
                            comment.getContent(),
                            comment.getCreatedDate(),
                            comment.getUser().getUsername()
                    ))
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "댓글 리스트 조회 성공", commentDto);
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage(), null);
        }
    }

    //댓글 삭제
    public ApiResponse deleteById(Long commentId, String token) {
        try {
            // JWT 토큰에서 사용자 이름 추출
            String username = jwtUtil.getUserNameFromToken(token);

            // 사용자 정보 조회
            User user = userService.findByUsername(username);
            Long userId = user.getUserId();

            Comment comment = findById(commentId);
            if (comment == null) {
                return new ApiResponse<>(false, "댓글을 찾을 수 없습니다.");
            }

            Long commentUserId = comment.getUser().getUserId();

            // 사용자와 댓글 작성자가 일치하는지 확인
            if (!userId.equals(commentUserId)) {
                return new ApiResponse<>(false, "댓글 삭제 권한이 없습니다.");
            }

            commentRepository.deleteById(commentId);
            return new ApiResponse<>(true, "댓글 삭제 성공");
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage());
        }
    }

    //댓글 수정
    @Override
    public ApiResponse updateById(Long commentId, String token, UpdateCommentDTO commentDto) {
        try {
            // JWT 토큰에서 사용자 이름 추출
            String username = jwtUtil.getUserNameFromToken(token);

            // 사용자 정보 조회
            User user = userService.findByUsername(username);
            Long userId = user.getUserId();

            Comment comment = findById(commentId);
            if (comment == null) {
                return new ApiResponse<>(false, "댓글을 찾을 수 없습니다.");
            }

            Long commentUserId = comment.getUser().getUserId();

            // 사용자와 댓글 작성자가 일치하는지 확인
            if (!userId.equals(commentUserId)) {
                return new ApiResponse<>(false, "댓글 수정 권한이 없습니다.");
            }

            // 댓글 수정
            comment.setContent(commentDto.getContent());
            comment.setCreatedDate(LocalDateTime.now());
            commentRepository.save(comment);

            return new ApiResponse<>(true, "댓글 수정 성공");
        } catch (Exception e) {
            return new ApiResponse<>(false, "내부 서버 오류: " + e.getMessage());
        }
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

}
