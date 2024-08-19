package com.example.gotsaeng_back.domain.record.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRecordResponseDto {
    private Long recordId;
    private Long recordTypeId;
    private LocalDate date;
    private String value;


    public static MonthlyRecordResponseDto fromEntity(Record record) {
        return RecordResponseDto.builder()
                .recordId(record.getRecordId())
                .recordTypeId(record.getRecordType().getRecordTypeId())
                .date(record.getDate())
                .value(record.getValue())
                .build();
    }
}