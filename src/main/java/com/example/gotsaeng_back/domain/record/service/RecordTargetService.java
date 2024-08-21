package com.example.gotsaeng_back.domain.record.service;

import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;

import java.util.List;

public interface RecordTargetService {
    RecordTarget saveRecordTarget(RecordTargetRequestDto dto, String token);

    RecordTarget updateRecordTarget(Long targetId, RecordTargetRequestDto dto, String token);

    RecordTarget findDefaultTarget(Long recordTypeId, String token);

    RecordTarget findCustomTarget(Long customRecordTypeId, String token);

    List<RecordTarget> findAllDefaultTargets(Long recordTypeId, String token);

    List<RecordTarget> findAllCustomTargets(Long customRecordTypeId, String token);
}
