package com.example.gotsaeng_back.domain.record.service;

import com.example.gotsaeng_back.domain.record.entity.RecordType;

import java.util.List;

public interface RecordTypeService {
    RecordType getRecordTypeByRecordTypeId(Long recordTypeId);
    List<RecordType> getAllRecordTypes();
}
