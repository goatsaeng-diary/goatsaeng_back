package com.example.gotsaeng_back.domain.auth.service.impl;

import static com.example.gotsaeng_back.global.exception.ExceptionEnum.FOLLOW_FAIL;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.UNFOLLOW_FAIL;

import com.example.gotsaeng_back.domain.auth.dto.FollowDto;
import com.example.gotsaeng_back.domain.auth.entity.Follow;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.repository.FollowRepository;
import com.example.gotsaeng_back.domain.auth.service.FollowService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.exception.ApiException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    public void doFollow(Long followerId, Long userId) {
        try{
            User followerUser = userService.findById(followerId);
            User followingUser = userService.findById(userId);
            Follow check = followRepository.findByFollowerAndFollowing(followerUser,followingUser);
            if(check!=null){
                throw new ApiException(FOLLOW_FAIL);
            }
            Follow follow = new Follow();
            follow.setFollower(followerUser);
            follow.setFollowing(followingUser);
            followRepository.save(follow);
        }catch (Exception e){
            throw new ApiException(FOLLOW_FAIL);
        }
    }

    @Override
    public void unFollow(Long followerId, Long userId) {
        try{
            User followerUser = userService.findById(followerId);
            User followingUser = userService.findById(userId);
            Follow check = followRepository.findByFollowerAndFollowing(followerUser,followingUser);
            if(check!=null){
                throw new ApiException(FOLLOW_FAIL);
            }
            Follow follow = new Follow();
            follow.setFollower(followerUser);
            follow.setFollowing(followingUser);
            followRepository.delete(follow);
        }catch (Exception e){
            throw new ApiException(UNFOLLOW_FAIL);
        }
    }

    @Override
    public List<FollowDto> getFollowerList(Long followingId) {
        try{
            User followingUser = userService.findById(followingId);
            List<Follow> followers = followRepository.findAllByFollower(followingUser);
            List<FollowDto> followDtos = new ArrayList<>();
            for(Follow follow : followers){
                User followerUser = follow.getFollowing();
                FollowDto followDto = new FollowDto();
                followDto.setNickname(followerUser.getNickname());
                followDto.setUserImage(followerUser.getUserImage());
                followDto.setUserId(followerUser.getUserId());
                followDtos.add(followDto);
            }
            return followDtos;
        }catch(Exception e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FollowDto> getFollowingList(Long followerId) {
        try{
            User followingUser = userService.findById(followerId);
            List<Follow> followings = followRepository.findAllByFollowing(followingUser);
            List<FollowDto> followingDtos = new ArrayList<>();
            for(Follow follow : followings){
                User followerUser = follow.getFollower();
                FollowDto followDto = new FollowDto();
                followDto.setNickname(followerUser.getNickname());
                followDto.setUserImage(followerUser.getUserImage());
                followDto.setUserId(followerUser.getUserId());
                followingDtos.add(followDto);
            }
            return followingDtos;
        }catch(Exception e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }


}
