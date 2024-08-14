package com.example.myapp.service;

import com.example.myapp.model.Log;
import com.example.myapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    // Create
    public Log createLog(Log log) {
        return logRepository.save(log);
    }

    // Read All
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    // Read by ID
    public Optional<Log> getLogById(Long id) {
        return logRepository.findById(id);
    }

    // Update
    public Log updateLog(Long id, Log logDetails) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        log.setDate(logDetails.getDate());
        log.setSentence(logDetails.getSentence());
        return logRepository.save(log);
    }

    // Delete
    public void deleteLog(Long id) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        logRepository.delete(log);
    }

    // Get all logs with row numbers
    public List<Map<String, Object>> getAllLogsWithRowNum() {
        List<Object[]> logs = logRepository.findAllWithRowNum();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] log : logs) {
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("rownum", log[0]);
            logMap.put("id", log[1]);
            logMap.put("date", log[2]);
            logMap.put("sentence", log[3]);
            result.add(logMap);
        }

        return result;
    }
}
