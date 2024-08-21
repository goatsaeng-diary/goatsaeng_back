package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostDetailDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostListDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);

    void editPost(Long postId, List<MultipartFile> files, PostEditDTO postEditDTO);

    void deletePost(Long postId);

    Post createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files, String token);

    PostListDTO userPost(Long userId);

    PostDetailDTO postDetails(Post post,String token);

    PostListDTO allPosts();
}
