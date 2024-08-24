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

    /**
     * 로그 저장 메서드
     * @param inputText 입력된 텍스트
     * @param outputText 출력된 텍스트
     * @param isFinalOutput 최종 문장 여부
     */
    public void saveLog(String inputText, String outputText, boolean isFinalOutput) {
        Log log = new Log();
        log.setLogDate(Timestamp.valueOf(LocalDateTime.now()));
        log.setInputText(inputText);
        log.setOutputText(outputText);

        logRepository.save(log);

        // 로그 정보 출력 (디버깅 용도)
        System.out.println("RowNum: " + log.getRowNum() +
                ", Date: " + log.getLogDate() +
                ", InputText: " + log.getInputText() +
                ", OutputText: " + log.getOutputText());
    }

    /**
     * 로그를 내림차순으로 정렬하여 반환하는 메서드
     * @return 정렬된 로그 리스트
     */
    public List<Log> getAllLogsOrderedByDateDesc() {
        return logRepository.findAllLogsOrderedByDateDesc();
    }

    /**
     * 모든 로그를 행 번호와 함께 조회
     * @return 로그 리스트
     */
    public List<Map<String, Object>> getAllLogsWithRowNum() {
        List<Log> logs = getAllLogsOrderedByDateDesc(); // 기존 로그 조회 메서드를 활용
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < logs.size(); i++) {
            Log log = logs.get(i);
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("rownum", i + 1); // 행 번호 설정
            logMap.put("logDate", log.getLogDate());
            logMap.put("inputText", log.getInputText());
            logMap.put("outputText", log.getOutputText());
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
