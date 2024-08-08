package com.example.gotsaeng_back.domain.post.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.dto.comment.ShowCommentDTO;
import com.example.gotsaeng_back.domain.post.dto.comment.UpdateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.domain.post.repository.CommentRepository;
import com.example.gotsaeng_back.domain.post.service.CommentService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.domain.post.dto.comment.CreateCommentDTO;
import com.example.gotsaeng_back.domain.post.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void save(CreateCommentDTO commentDTO, String token, Long postId) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);
        Post post = postService.getByPostId(postId);

        if (user == null || post == null) {
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<ShowCommentDTO> findByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostPostId(postId);
        if (comments.isEmpty()) {
            throw new ApiException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        return comments.stream()
                .map(comment -> new ShowCommentDTO(
                        comment.getCommentId(),
                        comment.getPost().getPostId(),
                        comment.getContent(),
                        comment.getCreatedDate(),
                        comment.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionEnum.COMMENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteById(Long commentId, String token) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Comment comment = findById(commentId);

        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.COMMENT_DELETE_FORBIDDEN);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void updateById(Long commentId, String token, UpdateCommentDTO commentDto) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Comment comment = findById(commentId);

        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.COMMENT_UPDATE_FORBIDDEN);
        }

        comment.setContent(commentDto.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
