package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    /**
     * 게시물 생성
     * @param postCreateDTO     게시물 생성시 필요한 요소 (제목, 내용)
     * @return 생성한 게시물 Id
     */
    @PostMapping("/create")
    public CustomResponse<Long> createPost(@RequestBody PostCreateDTO postCreateDTO, @RequestHeader("Authorization") String token) {
        Post post = postService.createPost(postCreateDTO,token);
        return new CustomResponse<>(HttpStatus.OK, "게시물 작성 성공", post.getPostId());
    }

    /**
     * 게시물 수정 페이지 이동
     * @param postId    수정할 게시물 Id
     * @return 수정할 게시물 Id
     */
    @GetMapping("/editpage/{postId}")
    public CustomResponse<Long> editPage(@PathVariable Long postId) {
        return new CustomResponse<>(HttpStatus.OK, "원래 게시물 불러오기", postService.getByPostId(postId).getPostId());
    }

    /**
     * 게시물 수정
     * @param postId    수정할 게시물 Id
     * @param postEditDTO   게시물 수정에 필요한 요소 (제목, 내용)
     * @return 수정한 게시물 Id
     */
    @PostMapping("/edit/{postId}")
    public CustomResponse<Void> editPost(@PathVariable Long postId, @RequestBody PostEditDTO postEditDTO) {
        postService.editPost(postId, postEditDTO);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 삭제 폼 요청",postId), null);
    }

    /**
     * 게시물 삭제
     * @param postId    삭제할 게시물 Id
     * @return  삭제할 게시물 Id
     */
    @DeleteMapping("/delete/{postId}")
    public CustomResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 삭제 완료", postId), null);
    }

}