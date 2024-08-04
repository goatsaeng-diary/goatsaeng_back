package com.example.gotsaeng_back.post.controller;

import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.post.dto.PostCreateDTO;
import com.example.gotsaeng_back.post.entity.Post;
import com.example.gotsaeng_back.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ApiResponse<?> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
//        post.setUser();
        post.setContent(postCreateDTO.getContent());
        postService.savePost(post);

        return new ApiResponse<>(true, "게시물 작성 성공");
    }

    @DeleteMapping("/delete/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return new ApiResponse<>(true, "게시물 삭제 성공");
    }

//    @GetMapping("/list")
//    public ApiResponse<?> showFollowingPosts() {
//
//    }
}
