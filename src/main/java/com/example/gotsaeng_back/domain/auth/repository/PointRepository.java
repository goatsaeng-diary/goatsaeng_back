package com.example.gotsaeng_back.domain.auth.repository;

import com.example.gotsaeng_back.domain.auth.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
}
