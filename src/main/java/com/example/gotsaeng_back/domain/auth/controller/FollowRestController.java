package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.FollowDto;
import com.example.gotsaeng_back.domain.auth.service.FollowService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.global.response.CustomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowRestController {
    private final FollowService followService;
    private final JwtUtil jwtUtil;

    //팔로우하기
    @Transactional
    @PostMapping("/doFollow")
    public CustomResponse<Void> doFollow(@PathVariable("followerId") Long followerId , @RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        followService.doFollow(followerId , userId);
        return new CustomResponse<>(HttpStatus.OK,"팔로우가 완료되었습니다.",null);
    }
    //언팔로우
    @Transactional
    @DeleteMapping("/unFollow")
    public CustomResponse<Void> unFollow(@PathVariable("followerId") Long followerId , @RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        followService.unFollow(followerId,userId);
        return new CustomResponse<>(HttpStatus.OK,"언팔로우 되었습니다.",null);
    }
    //팔로워리스트
    @GetMapping("/follower-list/{userId}")
    public CustomResponse<List<FollowDto>> followerList(@PathVariable("userId") Long followingId){
        List<FollowDto> followerList = followService.getFollowerList(followingId);
        return new CustomResponse<>(HttpStatus.OK,"팔로워 리스트",followerList);
    }
    //팔로잉리스트
    @GetMapping("/following-list/{userId}")
    public CustomResponse<List<FollowDto>> followingList(@PathVariable("userId") Long followerId){
        List<FollowDto> followerList = followService.getFollowingList(followerId);
        return new CustomResponse<>(HttpStatus.OK,"팔로잉 리스트",followerList);
    }
}
