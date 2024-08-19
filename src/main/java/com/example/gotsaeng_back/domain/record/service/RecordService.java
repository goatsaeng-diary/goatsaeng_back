package com.example.gotsaeng_back.domain.record.service;

import com.example.gotsaeng_back.domain.record.dto.request.RecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.RecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService {
    Record saveRecord(RecordRequestDto dto, String token, Long recordTypeId);
    Record updateRecord(RecordRequestDto dto, String token, Long recordId);
    void deleteRecord(String token, Long recordId);
    Record findByRecordId(Long recordId);
    Record findByRecordUser(String token);
    Page<RecordResponseDto> getRecordForCurrentMonth(String token, Pageable pageable);
    Page<MonthlyRecordResponseDto> getRecordsByMonth(String token, Pageable pageable);
}
