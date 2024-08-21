package com.example.gotsaeng_back.global.gptapi.controller;

import com.example.gotsaeng_back.global.gptapi.WordRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.entity.Word;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;
    private final JwtUtil jwtUtil;
    private final WordRepository wordRepository;

    // GPT API 호출을 처리하는 엔드포인트
    @PostMapping("/gpt")
    public Mono<GPTResponseDto> getGptResponse(
            @RequestHeader("Authorization") String userAccessToken // 클라이언트에서 전달된 사용자 인증 토큰
    ) {
        String username = jwtUtil.getUserNameFromToken(userAccessToken);
        String prompt = gptService.getQuestion();
        System.out.println(username);
        // GPT 요청을 위한 DTO 생성
        GPTRequestDto requestDto = new GPTRequestDto(prompt);
        // 서비스 호출하여 GPT 응답을 받음
        return gptService.getGPTResponse(requestDto, userAccessToken);
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
