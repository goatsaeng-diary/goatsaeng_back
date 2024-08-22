package com.example.gotsaeng_back.domain.post.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUser(User user);

    Post findByPostId(Long postId);

    List<Post> findPostsByUser(User user);

    // 오늘 만든 게시물 중 좋아요, 조회수, 댓글 순으로 가중치를 줘서 점수가 제일 큰 거 순으로 정렬해서 모으기
    @Query("SELECT p, COUNT(DISTINCT l) * 2 + COUNT(DISTINCT c) * 3 + p.viewCount " +
            "FROM Post p LEFT JOIN p.likes l LEFT JOIN p.comments c " +
            "WHERE p.createdDate >= CURRENT_DATE " +
            "GROUP BY p")
    List<Object[]> findPostsAndScores();

}
