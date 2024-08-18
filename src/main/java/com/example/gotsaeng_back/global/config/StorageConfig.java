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

    @Value("${aws.access_key_id}")
    private String accessKey;

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${s3.bucket}")
    private String bucketName;

    @Bean(name = "localStorageService")
    @Profile("dev")
    public FileStorageService localStorageService() {
        return new LocalStorageService(localPath);
    }

    @Bean(name = "s3StorageService")
    @Profile("prod")
    public FileStorageService s3StorageService() {
        return new S3StorageService(accessKey,secretKey,bucketName);
    }
}