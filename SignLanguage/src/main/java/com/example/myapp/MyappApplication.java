package com.example.myapp;

import com.example.myapp.dto.LogDto;
import com.example.myapp.model.Log;
import com.example.myapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MyappApplication implements CommandLineRunner {

    @Autowired
    private LogRepository logRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 예시 데이터 생성
        Log log1 = new Log();
        log1.setDate(LocalDateTime.now());
        log1.setSentence("첫 번째 로그 문장입니다.");
        logRepository.save(log1);

        Log log2 = new Log();
        log2.setDate(LocalDateTime.now());
        log2.setSentence("두 번째 로그 문장입니다.");
        logRepository.save(log2);

        // 저장된 데이터를 조회하여 LogDto로 변환
        List<Log> logs = logRepository.findAll();
        List<LogDto> logDtos = new ArrayList<>();

        int rowNum = 1;
        for (Log log : logs) {
            LogDto logDto = new LogDto(rowNum++, log.getDate(), log.getSentence());
            logDtos.add(logDto);
        }

        // LogDto를 출력 (콘솔에 출력하거나 필요한 다른 작업 수행)
        for (LogDto logDto : logDtos) {
            System.out.println("RowNum: " + logDto.getRowNum() + ", Date: " + logDto.getDate() + ", Sentence: " + logDto.getSentence());
        }
    }
}
