package com.example.gotsaeng_back.domain.post.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUser(User user);

    Post findByPostId(Long postId);
}
