package com.example.gotsaeng_back.global.file;

import com.example.gotsaeng_back.domain.post.service.PostService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    String storeFile(MultipartFile file, String directory) throws IOException;

    List<String> storeFiles(List<MultipartFile> files, String directory);
}
