package com.example.gotsaeng_back.domain.post.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
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
import com.example.gotsaeng_back.domain.post.entity.RecordType;
import com.example.gotsaeng_back.domain.post.repository.PostRepository;
import com.example.gotsaeng_back.domain.post.service.LikeService;
import com.example.gotsaeng_back.domain.post.service.PostService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.file.S3StorageService;
import com.example.gotsaeng_back.global.gptapi.dto.response.ChatGPTResponse;
import com.example.gotsaeng_back.global.gptapi.service.impl.AiCallService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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
    private final AiCallService aiCallService;

    @Transactional
    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post getByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
    }

    @Transactional
    @Override
    public void editPost(Long postId, List<MultipartFile> files, PostEditDTO postEditDTO) {
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

    public Post createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files, String token) {
        // 파일의 개수가 짝수인지 확인
        if (files.size() % 2 != 0) {
            throw new ApiException(ExceptionEnum.END_NOT_FOUND);
        }

        String type = null;
        for (int i = 0; i < files.size(); i++) {
            try {
                ChatGPTResponse response = aiCallService.requestImageAnalysis(files.get(i), "이 사진은 무엇을 인증하고 있나요? 운동을 인증하는거 같으면 '운동', 공부를 인증하는거 같으면 '공부', 아무것도 아닌거같으면 'none'이라는 대답만 보내줘.");
                String content = response.getChoices().getFirst().getMessage().getContent();
                if (content.equals("none")) {
                    throw new ApiException(ExceptionEnum.DIFFERENT_TYPE_FILE);
                }
                if (type != null && !type.equals(content)) {
                    throw new ApiException(ExceptionEnum.DIFFERENT_TYPE_FILE);
                }
                type = content;
            } catch (IOException e) {
                throw new ApiException(ExceptionEnum.FILE_NOT_FOUND);
            }
        }
        RecordType recordType = null;
        if (type.equals("운동")) {
            recordType = RecordType.EXERCISE;
        } else{
            recordType = RecordType.STUDY;
        }



        // 각 파일의 GPS 정보 및 시간 정보 추출
        List<GeoLocation> geoLocations = new ArrayList<>();
        List<Date> captureDates = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());

                // GPS 정보 추출
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
                    geoLocations.add(gpsDirectory.getGeoLocation());
                } else {
                    throw new ApiException(ExceptionEnum.DISTANCE_NOT_FOUND);
                }

                // 촬영 시간 추출
                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                Date captureDate = null;
                if (directory != null) {
                    captureDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    captureDates.add(captureDate);
                } else {
                    throw new ApiException(ExceptionEnum.TIME_NOT_FOUND);
                }

            } catch (ImageProcessingException | IOException e) {
                throw new ApiException(ExceptionEnum.FILE_NOT_FOUND);
            }
        }

        // 거리 및 시간 검증 및 파일 업로드
        List<String> validFiles = validateFilesAndUpload(files, geoLocations, captureDates);

        // 게시물 생성 및 저장
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        return savePost(Post.builder()
                .files(validFiles)
                .recordType(recordType)
                .content(postCreateDTO.getContent())
                .createdDate(LocalDateTime.now())
                .title(postCreateDTO.getTitle())
                .user(user)
                .build());
    }

    // 파일의 거리 및 시간 검증을 수행하고 업로드하는 메서드
    private List<String> validateFilesAndUpload(List<MultipartFile> files, List<GeoLocation> geoLocations, List<Date> captureDates) {
        List<String> validFiles = new ArrayList<>();
        for (int i = 0; i < geoLocations.size() - 1; i += 2) {
            // 거리 계산
            double distance = calculateDistance(
                    geoLocations.get(i).getLatitude(), geoLocations.get(i).getLongitude(),
                    geoLocations.get(i + 1).getLatitude(), geoLocations.get(i + 1).getLongitude()
            );

            // 시간 비교
            if (captureDates.get(i).after(captureDates.get(i + 1))) {
                throw new ApiException(ExceptionEnum.TIME_INCONSISTENT);
            }

            if (distance <= 50.0) {
                MultipartFile start = files.get(i);
                MultipartFile end = files.get(i + 1);

                s3StorageService.uploadFile(start);
                s3StorageService.uploadFile(end);

                validFiles.add(start.getOriginalFilename());
                validFiles.add(end.getOriginalFilename());
            } else {
                throw new ApiException(ExceptionEnum.DISTANCE_OVER_RANGE);
            }
        }

        return validFiles;
    }

    // 거리 계산 함수 (Haversine 공식 사용)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반경 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // 거리 (미터 단위)
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

    /**
     * TODO
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
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
                .postId(post.getPostId())
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
    public Page<PostDetailDTO> allPosts(int page, int size, String token) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageRequest);
        return getPosts(posts.getContent(), pageRequest, token);
    }

    @Override
    public Page<PostDetailDTO> getPosts(List<Post> posts, PageRequest pageRequest, String token) {
        List<PostDetailDTO> postDetailDTOList = posts.stream().map(post -> PostDetailDTO.builder()
                        .postId(post.getPostId())
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
