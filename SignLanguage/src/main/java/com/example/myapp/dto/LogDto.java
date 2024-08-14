package com.example.myapp.dto;

import java.time.LocalDateTime;

public class LogDto {
    private int rowNum;
    private LocalDateTime date;
    private String sentence;

    public LogDto(int rowNum, LocalDateTime date, String sentence) {
        this.rowNum = rowNum;
        this.date = date;
        this.sentence = sentence;
    }

    // Getters and Setters
    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
