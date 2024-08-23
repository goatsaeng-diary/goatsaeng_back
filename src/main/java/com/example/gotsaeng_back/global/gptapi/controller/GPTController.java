package com.example.gotsaeng_back.global.gptapi.controller;

import com.example.gotsaeng_back.domain.study.entity.Study;
import com.example.gotsaeng_back.domain.study.service.StudyService;
import com.example.gotsaeng_back.global.gptapi.WordRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.entity.Word;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;
    private final WordRepository wordRepository;
    private final StudyService studyService;

    // GPT API 호출을 처리하는 엔드포인트
    @Scheduled(fixedRate = 28800000) // 28,800,000 milliseconds = 8 hours
    public void scheduledTask() {
        boolean existStudy = studyService.existsByToday(LocalDate.now());
        System.out.println(existStudy);
        if (!existStudy) {
            Word word = gptService.getRandomWord();
            String prompt = gptService.getQuestion(word);
            GPTRequestDto requestDto = new GPTRequestDto(prompt);
            gptService.getGPTResponse(requestDto, word)
                    .subscribe(response -> {
                        // Asynchronous handling of response
                        System.out.println("Word from response: " + word.getWordName());
                    }, error -> {
                        // Handle errors if necessary
                        System.err.println("Error occurred: " + error.getMessage());
                    });
        }
    }


    @PostMapping("/wordInsert")
    public void fileInsert(){
        String filePath = "/Users/jeonghohyeon/Downloads/words.xls";

        Set<String> wordsInColumnB = readExcelColumnB(filePath);

        // 출력
        System.out.println("Words in Column B:");
        wordsInColumnB.forEach(word->{
            Word word1 = new Word();
            word1.setWordName(word);
            wordRepository.save(word1);
        });
        System.out.println("끝");
    }
    public static Set<String> readExcelColumnB(String filePath) {
        Set<String> wordsInColumnB = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옴
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(1); // 두 번째 열 (0부터 시작)

                if (cell != null) {
                    cell.setCellType(CellType.STRING); // 강제로 문자열로 변환
                    String valueB = cell.getStringCellValue();

                    // 숫자 제거
                    String filteredValue = removeNumbers(valueB);
                    if(filteredValue.length()>1){
                        wordsInColumnB.add(filteredValue);
                    }
                }
            }

        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }

        return wordsInColumnB;
    }
    private static String removeNumbers(String input) {
        // 숫자를 제거하는 로직
        return input.replaceAll("\\d", "");
    }
}
