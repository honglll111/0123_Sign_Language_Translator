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
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    // 최종 문장 수신을 위한 POST 엔드포인트
    @PostMapping("/receiveOutput")
    public ResponseEntity<Map<String, String>> receiveOutput(@RequestBody Map<String, String> payload) {
        String messageText = payload.get("text");
        boolean isFinalOutput = messageText.startsWith("FINAL_OUTPUT:");

        if (isFinalOutput) {
            messageText = messageText.replace("FINAL_OUTPUT:", "").trim();
            logService.saveCaption(messageText, true); // 최종 문장으로 저장
        } else {
            logService.saveCaption(messageText, false); // 중간 단어로 저장
        }

        return ResponseEntity.ok(Map.of("status", "success"));
    }

    // 로그를 표시하기 위한 GET 엔드포인트
    @GetMapping
    public String getAllLogs(Model model) {
        List<Map<String, Object>> logs = logService.getAllLogsWithRowNum();
        model.addAttribute("logs", logs);
        return "records"; // records.html로 이동
    }

    // Get the latest log (Single)
    @GetMapping("/latest")
    public String getLatestLog(Model model) {
        Log latestLog = logService.getLatestLog().orElse(null);
        model.addAttribute("latestLog", latestLog);
        return "records";
    }

    // Update - 로그 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Log> updateLog(@PathVariable Long id, @RequestBody Log logDetails) {
        Log updatedLog = logService.updateLog(id, logDetails);
        return ResponseEntity.ok(updatedLog);
    }

    // Delete - 로그 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    // Get all logs with row numbers
    @GetMapping("/with-rownum")
    public List<Map<String, Object>> getAllLogsWithRowNum() {
        return logService.getAllLogsWithRowNum();
    }
}
