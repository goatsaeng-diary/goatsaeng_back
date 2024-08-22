package com.example.gotsaeng_back.domain.post.service.impl;

import com.example.gotsaeng_back.domain.auth.dto.FollowDto;
import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.FollowService;
import com.example.gotsaeng_back.domain.auth.service.HistoryService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.dto.post.PostCreateDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostDetailDTO;
import com.example.gotsaeng_back.domain.post.dto.post.PostEditDTO;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.domain.post.repository.PostRepository;
import com.example.gotsaeng_back.domain.post.service.LikeService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.file.S3StorageService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3StorageService s3StorageService;
    private final LikeService likeService;
    private final HistoryService historyService;
    private final FollowService followService;

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
    @Override
    public void editPost(Long postId,List<MultipartFile> files,PostEditDTO postEditDTO) {
        Post post = getByPostId(postId);
        post.setTitle(postEditDTO.getTitle());
        post.setContent(postEditDTO.getContent());
        try {
            if (!files.isEmpty()) {
                List<String> list = files.stream()
                        .map(s3StorageService::uploadFile)
                        .toList();
                post.setFiles(list);
            } else post.setFiles(null);
            post.setUpdatedDate(LocalDateTime.now());
            savePost(post);
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.EDIT_NOT_COMPLETED);
        }

    }

    @Override
    @Transactional
    public Post createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files, String token) {
        Post post = new Post();
        try {
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
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.CREATE_NOT_COMPLETED);
        }

        return post;
    }

    @Override
    public Page<PostDetailDTO> recommendPosts(String token, int page, int size) {
        List<Object[]> results = postRepository.findPostsAndScores();
        User user = userService.findById(jwtUtil.getUserIdFromToken(token));
        List<Post> sortedPosts = results.stream()
                .map(result -> new AbstractMap.SimpleEntry<>((Post) result[0], (Long) result[1]))
                .filter(entry -> {
                    Post post = entry.getKey();
                    return post.getHistories().stream()
                            .noneMatch(history -> history.getUser().equals(user) && history.getViewDay().equals(LocalDate.now()));
                })
                .sorted(Map.Entry.<Post, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        return getPostDetailDTOS(token, page, size, sortedPosts);

    }

    private Page<PostDetailDTO> getPostDetailDTOS(String token, int page, int size, List<Post> sortedPosts) {
        int start = Math.min(page * size, sortedPosts.size());
        int end = Math.min((page + 1) * size, sortedPosts.size());
        List<Post> pageContentKeys = sortedPosts.subList(start, end);

        return getPosts(pageContentKeys, PageRequest.of(page, size), token);
    }

    public Page<PostDetailDTO> followingPosts(String token, int page, int size) {
        List<FollowDto> followingList = followService.getFollowingList(jwtUtil.getUserIdFromToken(token));
        List<Post> followingPosts = new ArrayList<>();
        followingList.forEach(followDto -> {
            User user = userService.findById(followDto.getUserId());
            followingPosts.addAll(postRepository.findAllByUser(user));
        });

        return getPostDetailDTOS(token, page, size, followingPosts);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        try {
            postRepository.deleteById(postId);
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.DELETE_NOT_COMPLETED);
        }

    }

    @Override
    public Page<PostDetailDTO> userPost(Long userId, int page, int size, String token) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userService.findById(userId);
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_FOUND);
        }
        List<Post> posts = postRepository.findAllByUser(user);
        return getPosts(posts, pageRequest, token);
    }

    @Override
    @Transactional
    public PostDetailDTO postDetails(Post post, String token) {
        User user = userService.findById(jwtUtil.getUserIdFromToken(token));
        boolean isContain = false;
        List<History> histories = post.getHistories();
        for (History history : histories) {
            if (history.getUser().equals(user) && history.getViewDay().equals(LocalDate.now())) {
                isContain = true;
                break;
            }
        }
        Long view = post.getViewCount();
        if (!isContain) {
            view++;
            try {
                historyService.saveHistory(History.builder()
                        .user(user)
                        .post(post)
                        .viewDay(LocalDate.now())
                        .build());
            } catch (Exception e) {
                throw new ApiException(ExceptionEnum.HISTORY_SAVE_NOT_COMPLETED);
            }

        }
        try {
            post.setViewCount(view);
            savePost(post);
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.VIEW_NOT_INCREASED);
        }

        boolean like = likeService.isLikePostByUser(post, token);
        return PostDetailDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .files(post.getFiles())
                .viewCount(view)
                .nickname(post.getUser().getNickname())
                .userImage(post.getUser().getUserImage())
                .createDate(post.getCreatedDate())
                .like(like)
                .commentCount((long) post.getComments().size())
                .likeCount(likeService.getLikes(post))
                .build();
    }

    @Override
    public Page<PostDetailDTO> allPosts(int page, int size,String token) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageRequest);
        return getPosts(posts.getContent(), pageRequest,token);
    }

    @Override
    public Page<PostDetailDTO> getPosts(List<Post> posts, PageRequest pageRequest,String token) {
        List<PostDetailDTO> postDetailDTOList = posts.stream().map(post -> PostDetailDTO.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .files(post.getFiles())
                        .nickname(post.getUser().getNickname())
                        .userImage(post.getUser().getUserImage())
                        .commentCount((long) post.getComments().size())
                        .likeCount(likeService.getLikes(post))
                        .viewCount(post.getViewCount())
                        .like(likeService.isLikePostByUser(post, token))
                        .build())
                        .toList();

        return new PageImpl<>(postDetailDTOList, pageRequest, postDetailDTOList.size());
    }
}
