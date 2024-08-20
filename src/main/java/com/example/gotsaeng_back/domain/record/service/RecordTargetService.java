package com.example.gotsaeng_back.domain.record.service;

import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;

public interface RecordTargetService {
    RecordTarget saveRecordTarget(RecordTargetRequestDto dto, String token);
    RecordTarget updateRecordTarget(Long targetId, RecordTargetRequestDto dto, String token);
}
