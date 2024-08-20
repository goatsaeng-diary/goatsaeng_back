package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTargetRepository extends JpaRepository<RecordTarget, Long> {
    RecordTarget findByUserAndRecordTypeOrCustomRecordType(User user, RecordType recordType, CustomRecordType customRecordType);
}
