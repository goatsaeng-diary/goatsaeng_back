package com.example.gotsaeng_back.domain.post.repository;

import com.example.gotsaeng_back.domain.post.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentCommentId(Long postId);
}
