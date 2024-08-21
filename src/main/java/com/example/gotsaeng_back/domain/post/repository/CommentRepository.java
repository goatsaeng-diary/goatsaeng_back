package com.example.gotsaeng_back.domain.post.repository;

import com.example.gotsaeng_back.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostPostId(Long postId);
}
