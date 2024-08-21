package com.example.gotsaeng_back.domain.record.dto.request;

import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTargetRequestDto {
    private RecordType recordType;
    private CustomRecordType customRecordType;
    @NotNull
    private Float target;
}
