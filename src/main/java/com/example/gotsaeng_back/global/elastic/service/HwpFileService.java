package com.example.gotsaeng_back.global.elastic.service;

import java.io.File;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import org.springframework.stereotype.Service;

@Service
public class HwpFileService {

    public String extractTextFromHwp(File file) {
        try {
            // HWP 파일 읽기
            HWPFile hwpFile = HWPReader.fromFile(file.getAbsolutePath());

            // 텍스트 추출 방법을 설정
            TextExtractMethod method = TextExtractMethod.InsertControlTextBetweenParagraphText;

            // 텍스트 추출
            return TextExtractor.extract(hwpFile, method);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
