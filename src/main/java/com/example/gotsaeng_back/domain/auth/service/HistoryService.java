package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.post.entity.Post;

import java.util.List;

public interface HistoryService {
    List<History> findHistoriesByUser(String token);
    List<History> findHistoriesByPost(Post post);
}
