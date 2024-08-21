package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.domain.post.dto.post.*;
import com.example.gotsaeng_back.domain.post.service.LikeService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.file.S3StorageService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.global.response.CustomResponse;
import com.example.gotsaeng_back.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    /**
     * 게시물 생성
     *
     * @param postCreateDTO 게시물 생성시 필요한 요소 (제목, 내용)
     * @return 생성한 게시물 Id
     */
    @PostMapping("/create")
    public CustomResponse<Long> createPost(@RequestPart(name = "postCreateDTO") PostCreateDTO postCreateDTO, @RequestHeader("Authorization") String token, @RequestPart(name = "files") List<MultipartFile> files) {
        Post post = postService.createPost(postCreateDTO, files, token);
        return new CustomResponse<>(HttpStatus.OK, "게시물 작성 성공", post.getPostId());
    }

    /**
     * 게시물 수정
     *
     * @param postId      수정할 게시물 Id
     * @param postEditDTO 게시물 수정에 필요한 요소 (제목, 내용)
     * @return 수정한 게시물 Id
     */
    @PostMapping("/edit/{postId}")
    public CustomResponse<Void> editPost(@PathVariable Long postId, @RequestBody PostEditDTO postEditDTO, @RequestParam(name = "lists") List<MultipartFile> files) {
        postService.editPost(postId, files, postEditDTO);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 수정", postId), null);
    }

    /**
     * 게시물 삭제
     *
     * @param postId 삭제할 게시물 Id
     * @return 삭제할 게시물 Id
     */
    @DeleteMapping("/delete/{postId}")
    public CustomResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 삭제 완료", postId), null);
    }

    /**
     * 사용자의 게시물 보기
     *
     * @param userId 게시물들의 사용자 Id
     * @return 해당 유저의 게시물들
     */
    @GetMapping("/list/{userId}")
    public CustomResponse<Page<PostDetailDTO>> showPosts(@PathVariable Long userId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestHeader("Authorization") String token) {
        Page<PostDetailDTO> postListDTO = postService.userPost(userId, page, size, token);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 사용자 게시물 로딩 완료", userId), postListDTO);
    }

    /**
     * 특정 게시물 보기
     *
     * @param postId 해당 게시물 Id
     * @return 해당 게시물 상세정보
     */
    @GetMapping("/view/{postId}")
    public CustomResponse<PostDetailDTO> postDetails(@PathVariable(name = "postId") Long postId, @RequestHeader("Authorization") String token) {
        Post post = postService.getByPostId(postId);
        PostDetailDTO postDetailDTO = postService.postDetails(post, token);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 로딩 완료", postId), postDetailDTO);
    }

    /**
     * 모든 게시물 보기
     *
     * @return 모든 게시물
     */
    @GetMapping("/list")
    public CustomResponse<Page<PostDetailDTO>> allPosts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestHeader("Authorization") String token) {
        Page<PostDetailDTO> postListDTO = postService.allPosts(page, size, token);
        return new CustomResponse<>(HttpStatus.OK, "모든 게시물 로딩 성공", postListDTO);
    }

    /**
     * @param postId 좋아요 누를 게시물 Id
     * @param like   좋아요 여부
     * @return
     */
    @PostMapping("/like/{postId}")
    public CustomResponse<Void> like(@PathVariable Long postId, @RequestBody boolean like, @RequestHeader("Authorization") String token) {
        Post post = postService.getByPostId(postId);
        if (like) {
            likeService.addLike(post, token);
        } else likeService.removeLike(post, token);

        return new CustomResponse<>(HttpStatus.OK, "좋아요 처리 완료", null);
    }

    @GetMapping("/like/list/{postId}")
    public CustomResponse<List<LikeUserDTO>> likeList(@PathVariable Long postId) {
        Post post = postService.getByPostId(postId);
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 게시물 좋아요 리스트", post.getPostId()), likeService.getLikeUsers(post));
    }

    @GetMapping("/like/list")
    public CustomResponse<List<Long>> likePostList(@RequestHeader("Authorization") String token) {
        return new CustomResponse<>(HttpStatus.OK, String.format("%d번 유저 좋아요 리스트", jwtUtil.getUserIdFromToken(token)), likeService.getLikePosts(token));
    }

    @GetMapping("/recommend")
    public CustomResponse<Page<PostDetailDTO>> recommendPosts(@RequestHeader("Authorization") String token, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<PostDetailDTO> recommendedPosts = postService.recommendPosts(token, page, size);
        return new CustomResponse<>(HttpStatus.OK, "추천 게시물 로딩 완료", recommendedPosts);
    }
}