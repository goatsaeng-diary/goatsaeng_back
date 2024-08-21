package com.example.gotsaeng_back.domain.auth.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.repository.HistoryRepository;
import com.example.gotsaeng_back.domain.auth.service.HistoryService;
import com.example.gotsaeng_back.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    @Override
    public List<History> findHistoriesByUser(User user) {
        return historyRepository.findHistoriesByUser(user);
    }

    @Override
    public List<History> findHistoriesByPost(Post post) {
        return historyRepository.findHistoriesByPost(post);
    }

    @Override
    public void saveHistory(History history) {
        historyRepository.save(history);
    }
}
