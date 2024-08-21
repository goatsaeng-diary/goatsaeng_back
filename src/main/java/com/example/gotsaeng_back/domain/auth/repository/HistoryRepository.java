package com.example.gotsaeng_back.domain.auth.repository;

import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findHistoriesByUser(User user);

    List<History> findHistoriesByPost(Post post);
}
