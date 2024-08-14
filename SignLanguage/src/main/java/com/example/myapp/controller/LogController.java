package com.example.myapp.controller;

import com.example.myapp.model.Log;
import com.example.myapp.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    // Create
    @PostMapping
    public Log createLog(@RequestBody Log log) {
        return logService.createLog(log);
    }

    // Read All
    @GetMapping
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        return logService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Log> updateLog(@PathVariable Long id, @RequestBody Log logDetails) {
        Log updatedLog = logService.updateLog(id, logDetails);
        return ResponseEntity.ok(updatedLog);
    }

    // Delete
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
