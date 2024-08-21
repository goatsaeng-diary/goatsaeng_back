package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.record.entity.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTypeRepository extends JpaRepository<RecordType, Long> {
}