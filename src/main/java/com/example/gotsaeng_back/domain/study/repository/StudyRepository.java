package com.example.gotsaeng_back.domain.study.repository;

import com.example.gotsaeng_back.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Study findByCreatedAt(LocalDate today);

    boolean existsByCreatedAt(LocalDate today);
}
