package com.example.gotsaeng_back.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {
    @Value("${storage.local.directory}")
    private String dir;

    @GetMapping("/C://Temp/upload/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            // 파일 경로를 생성하고, 리소스를 로드
            Path file = Paths.get(dir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // 예외 처리 하기
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            // 예외 처리 하기
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
