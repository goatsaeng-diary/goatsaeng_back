package com.example.gotsaeng_back.domain.auth.repository;

import com.example.gotsaeng_back.domain.auth.entity.Point;
import com.example.gotsaeng_back.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUser(User user);
}
