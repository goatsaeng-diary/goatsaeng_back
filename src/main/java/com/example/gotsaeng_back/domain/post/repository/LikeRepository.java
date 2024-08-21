package com.example.gotsaeng_back.domain.post.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.post.entity.Like;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteLikeByLikeUserAndPost(User user, Post post);

    List<Like> findLikesByPost(Post post);

    Optional<Like> findLikeByLikeUserAndPost(User user, Post post);

    List<Like> findLikesByLikeUser(User user);
}
