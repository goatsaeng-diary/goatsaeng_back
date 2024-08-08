package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.domain.post.dto.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")

    public CustomResponse<Post> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        postService.savePost(post);

        return new CustomResponse<>(true,"게시물 작성 성공");
   }
}
