package com.example.gotsaeng_back.domain.post.controller;

import com.example.gotsaeng_back.global.file.S3StorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class  ImageController {
    private final S3StorageServiceImpl s3StorageService;

    //s3 이미지 보여주기
    @GetMapping("/display")
    public ResponseEntity<StreamingResponseBody> display(@RequestParam("filename") String filename) {
        if (!StringUtils.hasText(filename)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        InputStream inputStream = s3StorageService.downloadFile(filename);

        StreamingResponseBody responseBody = outputStream -> {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };

        return ResponseEntity.ok()
                .body(responseBody);
    }
}
