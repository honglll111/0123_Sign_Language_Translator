package com.example.myapp;

import com.example.myapp.dto.LogDto;
import com.example.myapp.model.Log;
import com.example.myapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
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
        // 저장된 데이터를 조회하여 LogDto로 변환
        List<Log> logs = logRepository.findAll();
        List<LogDto> logDtos = new ArrayList<>();

        for (Log log : logs) {
            // LogDto 생성 시 inputText, outputText와 함께 sentence를 전달
            LogDto logDto = new LogDto(
                    log.getRowNum().intValue(), 
                    log.getLogDate(), 
                    log.getInputText(), // 중간 단어들
                    log.getOutputText(), // 최종 문장
                    log.getSentence() // sentence 추가
            );
            logDtos.add(logDto);
        }

        // LogDto를 출력 (콘솔에 출력하거나 필요한 다른 작업 수행)
        for (LogDto logDto : logDtos) {
            System.out.println("RowNum: " + logDto.getRowNum() + 
                               ", Date: " + logDto.getDate() + 
                               ", InputText: " + logDto.getInputText() + 
                               ", OutputText: " + logDto.getOutputText() + 
                               ", Sentence: " + logDto.getSentence()); // sentence 출력 추가
        }
    }
}
