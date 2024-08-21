package com.example.gotsaeng_back.domain.auth.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.History;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.repository.HistoryRepository;
import com.example.gotsaeng_back.domain.auth.service.HistoryService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.post.entity.Post;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public List<History> findHistoriesByUser(String token) {
        User user = userService.findById(jwtUtil.getUserIdFromToken(token));
        return historyRepository.findHistoriesByUser(user);
    }

    @Override
    public List<History> findHistoriesByPost(Post post) {
        return historyRepository.findHistoriesByPost(post);
    }
}
