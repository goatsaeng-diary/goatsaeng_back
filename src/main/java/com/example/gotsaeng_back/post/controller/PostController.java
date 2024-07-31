package com.example.gotsaeng_back.post.controller;

import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.post.dto.PostCreateDTO;
import com.example.gotsaeng_back.post.entity.Post;
import com.example.gotsaeng_back.post.service.PostService;
import com.example.gotsaeng_back.post.service.impl.PostServiceImpl;
import jakarta.persistence.Id;
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
    public ApiResponse<Post> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        postService.savePost(post);

        return new ApiResponse<>(true,"게시물 작성 성공");
    }
}
