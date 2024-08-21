package com.example.gotsaeng_back.domain.record.repository;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRecordTypeRepository extends JpaRepository<CustomRecordType, Long> {
    CustomRecordType findByUserAndTypeName(User user, String typeName);
    List<CustomRecordType> findAllByUser(User user);
}
