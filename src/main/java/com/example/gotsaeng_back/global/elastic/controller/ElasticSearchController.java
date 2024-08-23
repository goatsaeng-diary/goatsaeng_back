package com.example.gotsaeng_back.global.elastic.controller;

import com.example.gotsaeng_back.global.elastic.service.HwpFileService;
import com.example.gotsaeng_back.global.elastic.service.StudyElsService;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/doc")
@RequiredArgsConstructor
public class ElasticSearchController {

    private final RestHighLevelClient client;

    private final HwpFileService hwpFileService;

    private final GPTService gptService;

    private final StudyElsService studyElsService;

    //study 인덱스 file 업로드
    @PostMapping("/upload")
    public CustomResponse<Void> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);

            //hwp파일에서 text로 변환
            //if문 hwp , pdf 구분
            String content = hwpFileService.extractTextFromHwp(tempFile);
            System.out.println(content);
            // Save the extracted information using the StudyService
            studyElsService.saveStudy(file.getOriginalFilename(), content);
            //S3업로드

        }
        return new CustomResponse<>(HttpStatus.OK,"파일이 업로드 되었습니다",null);
    }


    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam("content") String text) throws IOException {
        SearchRequest searchRequest = new SearchRequest("study");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("content", text));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        String responseText;

        if (searchResponse.getHits().getHits().length > 0) {
            responseText = searchResponse.getHits().getHits()[0].getSourceAsString();
        } else {
            responseText = "Document not found, generating response from ChatGPT.";
        }

        System.out.println(responseText);
        return null;
    }

    @GetMapping("/search")
    public Mono<CustomResponse<GPTResponseDto>> getChatGptResponse(@RequestParam("content") String content) {
        return studyElsService.findByContentMatch(content)
                .map(dto -> new CustomResponse<>(HttpStatus.OK, "답변 결과입니다", dto))
                .onErrorResume(error -> {
                    // 오류가 발생했을 때 처리
                    System.err.println("Error occurred: " + error.getMessage());
                    return Mono.just(new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다", null));
                });
    }
}