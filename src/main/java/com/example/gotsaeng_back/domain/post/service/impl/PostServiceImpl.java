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
    private final FollowService followService;
    private final HistoryService historyService;

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
    public Page<PostDetailDTO> recommendPosts(String token, int page, int size) {

        List<FollowDto> following = followService.getFollowingList(jwtUtil.getUserIdFromToken(token));
        Map<Long, Long> map = new HashMap<>();


        following.stream().map(FollowDto::getUserId).toList().forEach(userId -> {
            List<Post> posts = new ArrayList<>();
            User user = userService.findById(userId);

            List<History> histories = historyService.findHistoriesByUser(user);
            histories.forEach(history ->  posts.add(history.getPost()));
            posts.forEach(post -> {
                Long score = post.getViewCount() + post.getComments().size() + post.getHistories().size() + followService.getFollowerList(userId).size();
                map.put(post.getPostId(), score);
            });
        });


        List<Long> sortedKeys = map.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();


        int start = Math.min(page * size, sortedKeys.size());
        int end = Math.min((page + 1) * size, sortedKeys.size());
        List<Long> pageContentKeys = sortedKeys.subList(start, end);


        List<PostDetailDTO> content = pageContentKeys.stream().map(postId -> {
            Post post = postRepository.findByPostId(postId);
            return PostDetailDTO.builder()
                    .nickname(post.getUser().getNickname())
                    .likeCount((long) post.getLikes().size())
                    .commentCount((long) post.getComments().size())
                    .createDate(post.getCreatedDate())
                    .content(post.getContent())
                    .viewCount(post.getViewCount())
                    .title(post.getTitle())
                    .like(likeService.isLikePostByUser(post, token))
                    .files(post.getFiles())
                    .userImage(post.getUser().getUserImage())
                    .build();
        }).toList();

        return new PageImpl<>(content, PageRequest.of(page, size), sortedKeys.size());
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<PostDetailDTO> userPost(Long userId, int page, int size, String token) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByUser(userService.findById(userId));
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
            History history = new History();
            history.builder()
                    .user(user)
                    .post(post)
                    .viewDay(LocalDate.now())
                    .build();
            historyService.saveHistory(history);
        }
        post.setViewCount(view);
        savePost(post);
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
