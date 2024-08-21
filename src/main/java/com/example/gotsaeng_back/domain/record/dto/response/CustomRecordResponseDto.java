package com.example.gotsaeng_back.domain.record.dto.response;

import com.example.gotsaeng_back.domain.record.entity.CustomRecord;
import com.example.gotsaeng_back.domain.record.entity.Record;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordResponseDto {
    private Long customRecordId;
    private Long customRecordTypeId;
    private LocalDate date;
    private String value;


    public static CustomRecordResponseDto fromEntity(CustomRecord customRecord) {
        return CustomRecordResponseDto.builder()
                .customRecordId(customRecord.getCustomRecordId())
                .customRecordTypeId(customRecord.getCustomRecordType().getCustomRecordTypeId())
                .date(customRecord.getDate())
                .value(customRecord.getValue())
                .build();
    }
}
