package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordTargetRepository extends JpaRepository<RecordTarget, Long> {

    boolean existsByUserAndRecordTypeAndDate(User user, RecordType recordType, LocalDate date);

    boolean existsByUserAndCustomRecordTypeAndDate(User user, CustomRecordType customRecordType, LocalDate date);

    RecordTarget findByUserAndRecordType_RecordTypeId(User user, Long recordTypeId);

    RecordTarget findByUserAndCustomRecordType_CustomRecordTypeId(User user, Long customRecordTypeId);

    List<RecordTarget> findAllByUserIdAndRecordTypeId(Long userId, Long recordTypeId);

    List<RecordTarget> findAllByUserIdAndCustomRecordTypeId(Long userId, Long customRecordTypeId);
}