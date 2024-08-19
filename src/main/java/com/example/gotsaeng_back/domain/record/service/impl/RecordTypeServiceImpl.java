package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.record.entity.RecordType;
import com.example.gotsaeng_back.domain.record.repository.RecordTypeRepository;
import com.example.gotsaeng_back.domain.record.service.RecordTypeService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordTypeServiceImpl implements RecordTypeService {
    private final RecordTypeRepository recordTypeRepository;

    //생성, 수정, 삭제 불필요

    //단일 가져오기
    @Override
    @Transactional(readOnly = true)
    public RecordType getRecordTypeByRecordTypeId(Long recordTypeId) {
        return recordTypeRepository.findById(recordTypeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_TYPE_NOT_FOUND));
    }

    //전체 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<RecordType> getAllRecordTypes() {
        return recordTypeRepository.findAll();
    }
}
