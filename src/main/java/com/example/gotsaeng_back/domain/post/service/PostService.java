package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostDetailDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostListDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;

import java.util.List;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);

    void editPost(Long postId, List<String> filePaths, PostEditDTO postEditDTO);

    void deletePost(Long postId);

    Post createPost(PostCreateDTO postCreateDTO,List<String> filePaths, String token);

    PostListDTO userPost(Long userId);

    PostDetailDTO postDetails(Long postId);

    PostListDTO allPosts();
}
