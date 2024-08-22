package com.example.gotsaeng_back.domain.post.service;

import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostDetailDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostListDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void savePost(Post post);

    Post getByPostId(Long postId);

    void editPost(Long postId, List<MultipartFile> files, PostEditDTO postEditDTO);

    void deletePost(Long postId);

    Post createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files, String token);

    Page<PostDetailDTO> userPost(Long userId, int page, int size,String token);

    PostDetailDTO postDetails(Post post,String token);

    Page<PostDetailDTO> allPosts(int page, int size,String token);

    Page<PostDetailDTO> getPosts(List<Post> posts, PageRequest pageRequest, String token);

    Page<PostDetailDTO> recommendPosts(String token, int page, int size);

}
