package com.example.gotsaeng_back.domain.post.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostDetailDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostListDTO;
import com.example.gotsaeng_back.domain.post.entity.Like;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.domain.post.repository.PostRepository;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.file.S3StorageService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3StorageService s3StorageService;

    @Transactional
    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post getByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(()-> new RuntimeException("게시물이 없습니다."));
    }

    @Transactional
    public void editPost(Long postId,List<MultipartFile> files,PostEditDTO postEditDTO) {
        Post post = getByPostId(postId);
        post.setTitle(postEditDTO.getTitle());
        post.setContent(postEditDTO.getContent());
        if (!files.isEmpty()) {
            List<String> list = files.stream()
                    .map(s3StorageService::uploadFile)
                    .toList();
            post.setFiles(list);
        }
        else post.setFiles(null);
        post.setUpdatedDate(LocalDateTime.now());
        savePost(post);
    }

    @Override
    @Transactional
    public Post createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files, String token) {
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        List<String> list = files.stream()
                .map(s3StorageService::uploadFile)
                .toList();
        post.setFiles(list);
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);
        post.setUser(user);
        savePost(post);
        return post;
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public PostListDTO userPost(Long userId) {
        List<Post> posts = postRepository.findAllByUser(userService.findById(userId));
        return getPosts(posts);
    }

    @Override
    public PostDetailDTO postDetails(Long postId) {
        Post post = postRepository.findByPostId(postId);
        return PostDetailDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .files(post.getFiles())
                .nickname(post.getUser().getNickname())
                .build();
    }

    @Override
    public PostListDTO allPosts() {
        List<Post> posts = postRepository.findAll();
        return getPosts(posts);
    }

    public PostListDTO getPosts(List<Post> posts) {
        PostListDTO postListDTO = new PostListDTO();
        List<PostDetailDTO> postDetailDTOList = postListDTO.getPosts();
        for (Post post : posts) {
            PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .files(post.getFiles())
                    .nickname(post.getUser().getNickname())
                    .build();
            postDetailDTOList.add(postDetailDTO);
        }
        postListDTO.setPosts(postDetailDTOList);
        return postListDTO;
    }


}
