package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    // 특정 사용자와 날짜에 대한 단일 기록 찾기
    List<Record> findByUserAndDate(User user, LocalDate date);

    // 사용자에 의해 생성된 모든 기록을 찾기
    List<Record> findAllByUser(User user);

    // 사용자와 날짜 범위에 따라 모든 기록을 페이지네이션
    Page<Record> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // 특정 사용자, 기록 유형 및 날짜에 대한 단일 기록 찾기
    Record findByUserAndRecordTypeAndDate(User user, RecordType recordType, LocalDate date);
}