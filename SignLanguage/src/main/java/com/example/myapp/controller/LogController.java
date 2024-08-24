package com.example.myapp.controller;

import com.example.myapp.model.Log;
import com.example.myapp.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")  // 기본 경로를 "/"로 설정
public class LogController {

    @Autowired
    private LogService logService;

    // 최종 문장 수신을 위한 POST 엔드포인트
    @PostMapping("/logs/receiveOutput")
    public ResponseEntity<Map<String, String>> receiveOutput(@RequestBody Map<String, String> payload) {
        String inputText = payload.get("inputText");
        String outputText = payload.get("outputText");

        if (inputText == null || outputText == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Missing inputText or outputText"));
        }

        // 최종 문장 여부 확인 (필요 시)
        boolean isFinalOutput = outputText.startsWith("FINAL_OUTPUT:");
        if (isFinalOutput) {
            outputText = outputText.replace("FINAL_OUTPUT:", "").trim();
        }

        // Log 저장
        logService.saveLog(inputText, outputText, isFinalOutput);

        return ResponseEntity.ok(Map.of("status", "success"));
    }

    // 로그를 표시하기 위한 GET 엔드포인트 (기존 RecordsController 기능 통합)
    @GetMapping("/records")  // 이제 "/records"로 바로 접근 가능
    public String showProjects(Model model) {
        List<Log> logs = logService.getAllLogsOrderedByDateDesc(); // DB에서 내림차순으로 정렬된 로그를 가져옴
        model.addAttribute("logs", logs);
        return "records"; // records.html로 이동
    }

    // Get the latest log (Single)
    @GetMapping("/logs/latest")
    public String getLatestLog(Model model) {
        Log latestLog = logService.getLatestLog().orElse(null);
        model.addAttribute("latestLog", latestLog);
        return "records";
    }

    // Update - 로그 업데이트
    @PutMapping("/logs/{id}")
    public ResponseEntity<Log> updateLog(@PathVariable Long id, @RequestBody Log logDetails) {
        Log updatedLog = logService.updateLog(id, logDetails);
        return ResponseEntity.ok(updatedLog);
    }

    // Delete - 로그 삭제
    @DeleteMapping("/logs/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    // Get all logs with row numbers
    @GetMapping("/logs/with-rownum")
    public List<Map<String, Object>> getAllLogsWithRowNum() {
        return logService.getAllLogsWithRowNum();
    }

    // 자막 저장을 위한 POST 엔드포인트
    @PostMapping("/logs/save-caption")
    public ResponseEntity<String> saveLog(@RequestBody Map<String, String> request) {
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
