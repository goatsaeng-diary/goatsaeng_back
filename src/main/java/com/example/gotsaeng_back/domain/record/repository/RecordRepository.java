package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findByUser(User user, LocalDate date);
    List<Record> findAllByUser(User user);
    List<Record> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    Record findByUserAndRecordTypeAndDate(User user, RecordType recordType, LocalDate date);
}