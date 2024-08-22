package com.example.myapp.service;

import com.example.myapp.model.Log;
import com.example.myapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class LogService {

    @Autowired
    private LogRepository logRepository;

    private String inputText = "";
    private String outputText = "";

    /**
     * 자막 저장 메서드
     * @param captionText 저장할 텍스트
     * @param isFinalOutput 최종 문장 여부
     */
    public void saveCaption(String captionText, boolean isFinalOutput) {
        Log log = new Log();
        log.setLogDate(Timestamp.valueOf(LocalDateTime.now()));

        this.inputText = captionText;
        log.setInputText(this.inputText);

        if (isFinalOutput) {
            this.outputText = captionText;
            log.setOutputText(this.outputText);
            log.setSentence(this.outputText);
        } else {
            log.setSentence(this.inputText); // 중간 문장을 sentence에 저장
        }

        logRepository.save(log);

        // 로그 정보 출력
        System.out.println("RowNum: " + log.getRowNum() +
                ", Date: " + log.getLogDate() +
                ", InputText: " + log.getInputText() +
                ", OutputText: " + log.getOutputText() +
                ", Sentence: " + log.getSentence());
    }

    /**
     * 입력된 텍스트 반환
     * @return inputText
     */
    public String getInputText() {
        return inputText;
    }

    /**
     * 최종 출력된 텍스트 반환
     * @return outputText
     */
    public String getOutputText() {
        return outputText;
    }

    /**
     * 모든 로그를 행 번호와 함께 조회
     * @return 로그 리스트
     */
    public List<Map<String, Object>> getAllLogsWithRowNum() {
        List<Log> logs = logRepository.findAllLogsOrderedByDateDesc();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < logs.size(); i++) {
            Log log = logs.get(i);
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("rownum", i + 1); // 행 번호 설정
            logMap.put("logDate", log.getLogDate());
            logMap.put("inputText", log.getInputText());
            logMap.put("outputText", log.getOutputText());
            logMap.put("sentence", log.getSentence()); // sentence 값 추가
            result.add(logMap);
        }

        return result;
    }

    /**
     * 최신 로그 조회
     * @return Optional<Log>
     */
    public Optional<Log> getLatestLog() {
        return logRepository.findTopByOrderByLogDateDesc();
    }

    /**
     * 로그 업데이트 메서드
     * @param id 로그 ID
     * @param logDetails 업데이트할 로그 상세 정보
     * @return 업데이트된 로그
     */
    public Log updateLog(Long id, Log logDetails) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));
        log.setLogDate(logDetails.getLogDate());
        log.setInputText(logDetails.getInputText());
        log.setOutputText(logDetails.getOutputText());
        log.setSentence(logDetails.getSentence()); // sentence 값 업데이트
        return logRepository.save(log);
    }

    /**
     * 로그 삭제 메서드
     * @param id 삭제할 로그 ID
     */
    public void deleteLog(Long id) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));
        logRepository.delete(log);
    }
}
