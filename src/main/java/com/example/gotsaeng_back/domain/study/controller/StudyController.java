package com.example.gotsaeng_back.domain.study.controller;

import com.example.gotsaeng_back.domain.study.entity.Study;
import com.example.gotsaeng_back.domain.study.service.StudyService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {
    private final StudyService studyService;

    @GetMapping("/get")
    public CustomResponse<Study> getStudy() {
        System.out.println(LocalDate.now());
        Study study = studyService.findByToday(LocalDate.now());
        System.out.println(study.getStudyId());
        return new CustomResponse<>(HttpStatus.OK, "오늘의 학습하기를 불러왔습니다", study);
    }
    @GetMapping("/answer")
    public CustomResponse<String> getAnswer(@RequestParam("answer") String answer){
        if(studyService.getAnswer(answer)){
            return new CustomResponse<>(HttpStatus.OK,"정답입니다",null);

        }else{
            return new CustomResponse<>(HttpStatus.OK,"틀렸습니다",null);
        }
    }
}
