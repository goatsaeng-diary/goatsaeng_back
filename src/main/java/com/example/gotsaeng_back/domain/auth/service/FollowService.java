package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.dto.FollowDto;
import com.example.gotsaeng_back.global.response.CustomResponse;
import java.util.List;

public interface FollowService {
    void doFollow(Long followerId, Long userId);

    void unFollow(Long followerId, Long userId);

    List<FollowDto> getFollowerList(Long followingId);

    List<FollowDto> getFollowingList(Long followerId);
}
