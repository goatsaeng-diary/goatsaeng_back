package com.example.gotsaeng_back.global.config;

import com.example.gotsaeng_back.global.file.FileStorageService;
import com.example.gotsaeng_back.global.file.LocalStorageService;
import com.example.gotsaeng_back.global.file.S3StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {
    @Value("${storage.local.directory}")
    private String localPath;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Bean(name = "localStorageService")
    @Profile("dev")
    public FileStorageService localStorageService() {
        return new LocalStorageService(localPath);
    }

}