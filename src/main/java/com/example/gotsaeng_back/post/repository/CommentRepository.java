package com.example.gotsaeng_back.post.repository;

import com.example.gotsaeng_back.post.entity.Comment;
import com.example.gotsaeng_back.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostPostId(Long postId);
}
