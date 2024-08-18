package com.example.gotsaeng_back.global.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class S3StorageService implements FileStorageService {

    private final AmazonS3 s3client;
    private final String bucketName;

    public S3StorageService(@Value("${aws.access_key_id}") String accessKey,
                            @Value("${aws.secret_access_key}") String secretKey,
                            @Value("${s3.bucket}") String bucketName) {
        this.bucketName = bucketName;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                // 지역 추가
                .withRegion("")
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Override
    public String storeFile(MultipartFile file, String directory) throws IOException {
        File tempFile = convertMultiPartToFile(file);
        String fileName = directory + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            s3client.putObject(new PutObjectRequest(bucketName, fileName, tempFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3client.getUrl(bucketName, fileName).toString();
        } catch (AmazonServiceException e) {
            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        } finally {
            tempFile.delete();
        }
    }

    @Override
    public List<String> storeFiles(List<MultipartFile> files, String directory) {
        return files.stream()
                .map(file -> {
                    try {
                        return storeFile(file, directory);
                    } catch (IOException e) {
                        // 예외 처리
                        throw new RuntimeException("Failed to store file in S3", e);
                    }
                })
                .collect(Collectors.toList());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
