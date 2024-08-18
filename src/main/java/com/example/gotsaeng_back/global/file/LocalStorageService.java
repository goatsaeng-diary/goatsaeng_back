package com.example.gotsaeng_back.global.file;

import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



public class LocalStorageService implements FileStorageService {



    @Value("${storage.local.directory}")
    private Path fileStorageLocation; // 파일 저장 위치

    public LocalStorageService(@Value("${storage.local.directory}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            // 나중에 예외 추가하기 (폴더 만들지 못함)
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String directory) throws IOException {

        // 파일 이름 생성
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);
        return directory + fileName;
    }

    @Override
    public List<String> storeFiles(List<MultipartFile> files, String directory) {
        return files.stream()
                .map(file -> {
                    try {
                        return storeFile(file, directory);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file locally", e);
                    }
                })
                .toList();
    }
}
