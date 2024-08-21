package com.example.gotsaeng_back.domain.auth.repository;

import com.example.gotsaeng_back.domain.auth.entity.Follow;
import com.example.gotsaeng_back.domain.auth.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findAllByFollowing(User user);
    List<Follow> findAllByFollower(User user);
    Follow findByFollowerAndFollowing(User follower , User following);

}
