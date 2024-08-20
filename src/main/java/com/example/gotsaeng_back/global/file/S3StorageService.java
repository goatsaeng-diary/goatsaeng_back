package com.example.gotsaeng_back.global.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface S3StorageService {
    String uploadFile(MultipartFile file);
    InputStream downloadFile(String key);
    void deleteFile(String file);
}
