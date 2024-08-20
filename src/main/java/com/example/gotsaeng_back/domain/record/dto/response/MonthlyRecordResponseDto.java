package com.example.gotsaeng_back.domain.record.dto.response;

import lombok.*;

import java.time.YearMonth;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRecordResponseDto {
    private Long recordTypeId;
    private String month;
    private int participationCount;
    private double averageValue;

    // 월별 통계 정보를 생성하는 메서드
    public static MonthlyRecordResponseDto fromMonthlySummary(YearMonth yearMonth, int participationCount, double averageValue) {
        return MonthlyRecordResponseDto.builder()
                .month(yearMonth.toString() + "-01")  // "YYYY-MM-01" 형식으로 문자열 반환
                .participationCount(participationCount)
                .averageValue(averageValue)
                .build();
    }
}