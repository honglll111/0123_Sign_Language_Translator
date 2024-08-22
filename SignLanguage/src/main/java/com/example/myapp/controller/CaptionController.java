package com.example.myapp.controller;

import com.example.myapp.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CaptionController {

    private final LogService logService;

    public CaptionController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/save-caption")
    public ResponseEntity<String> saveCaption(@RequestBody Map<String, String> request) {
        String caption = request.get("caption");

        // "FINAL_OUTPUT:" 접두어를 통해 input_text와 output_text를 구분
        boolean isFinalOutput = caption.startsWith("FINAL_OUTPUT:");
        String inputText = isFinalOutput ? "" : caption; // 중간 단어들
        String outputText = isFinalOutput ? caption.replace("FINAL_OUTPUT:", "").trim() : ""; // 최종 문장

        // LogService를 통해 log 테이블에 저장
        logService.saveLog(inputText, outputText, isFinalOutput);

        return ResponseEntity.ok("자막이 성공적으로 저장되었습니다.");
    }
}