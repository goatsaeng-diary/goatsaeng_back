package com.example.gotsaeng_back.global.elastic.controller;

import com.example.gotsaeng_back.global.elastic.service.HwpFileService;
import com.example.gotsaeng_back.global.elastic.service.StudyElsService;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ElasticSearchController {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private HwpFileService hwpFileService;

    @Autowired
    private GPTService gptService;

    @Autowired
    private StudyElsService studyElsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            // Create a temporary file and transfer the content to it
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);

            // Extract content from the temporary file
            String content = hwpFileService.extractTextFromHwp(tempFile);
            System.out.println(content);
            // Save the extracted information using the StudyService
            studyElsService.saveStudy(file.getOriginalFilename(), content);
        }
        return ResponseEntity.ok("Files uploaded successfully");
    }


    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String indexName, @RequestParam String text) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
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

        String gptResponse = getChatGptResponse(responseText);
        return ResponseEntity.ok(gptResponse);
    }

    private String getChatGptResponse(String prompt) {
        // OpenAI API 호출 코드
        return "ChatGPT response based on prompt: " + prompt;
    }
}