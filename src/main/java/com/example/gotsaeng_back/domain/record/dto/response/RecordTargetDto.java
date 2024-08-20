package com.example.gotsaeng_back.domain.record.dto.response;

import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTargetDto {
    private Long targetId;
    private Long recordTypeId;
    private Long customRecordTypeId;
    private Float target;
    private LocalDate date;


    public static RecordTargetDto fromEntity(RecordTarget recordTarget) {
        return RecordTargetDto.builder()
                .recordTypeId(recordTarget.getRecordType().getRecordTypeId())
                .customRecordTypeId(recordTarget.getCustomRecordType().getCustomRecordTypeId())
                .date(recordTarget.getDate())
                .target(recordTarget.getTarget())
                .build();
    }
}
