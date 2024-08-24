package com.example.gotsaeng_back.domain.study.controller;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.service.PointService;
import com.example.gotsaeng_back.domain.study.dto.StudyDto;
import com.example.gotsaeng_back.domain.study.entity.Study;
import com.example.gotsaeng_back.domain.study.service.StudyService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {
    private final StudyService studyService;
    private final PointService pointService;


    @GetMapping("/get")
    public CustomResponse<Study> getStudy() {
        Study study = studyService.findByToday(LocalDate.now());
        return new CustomResponse<>(HttpStatus.OK, "오늘의 학습하기를 불러왔습니다", study);
    }

    @PostMapping("/isCorrect")
    public CustomResponse<Void> correctAnswer(@RequestBody StudyDto studyDto, @RequestBody PointDto pointDto, @RequestHeader("Authorization") String token) {
        Study study = studyService.findByToday(LocalDate.now());
        if(studyService.isCorrect(studyDto, study)){
            pointService.save(pointDto, token);
            return new CustomResponse<>(HttpStatus.OK, "정답이 맞습니다", null);
        }else{
            return new CustomResponse<>(HttpStatus.OK, "정답이 아닙니다", null);
        }
    }
}
