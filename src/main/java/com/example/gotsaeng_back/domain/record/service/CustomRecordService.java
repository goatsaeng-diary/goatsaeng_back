package com.example.gotsaeng_back.domain.record.service;

import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.request.RecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecord;
import com.example.gotsaeng_back.domain.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomRecordService {
    CustomRecord saveCustomRecord(CustomRecordRequestDto dto, String token, Long customRecordTypeId);
    CustomRecord updateCustomRecord(CustomRecordRequestDto dto, String token, Long customRecordId);
    void deleteCustomRecord(String token, Long customRecordId);
    CustomRecord findByCustomRecordId(Long customRecordId);
    List<CustomRecord> findByCustomRecordUser(String token);
    Page<CustomRecordResponseDto> getCustomRecordForCurrentMonth(String token, Pageable pageable);
    Page<MonthlyRecordResponseDto> getCustomRecordsByMonth(String token, Long customRecordTypeId, Pageable pageable);

}
