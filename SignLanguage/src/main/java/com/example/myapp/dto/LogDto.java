package com.example.myapp.dto;

import java.sql.Timestamp;

public class LogDto {

    private int rowNum;
    private Timestamp date;
    private String inputText; // 중간 단어들 (검정색으로 표시)
    private String outputText; // 최종 문장 (파란색으로 표시)
    private String sentence; // 추가된 sentence 필드

    public LogDto(int rowNum, Timestamp date, String inputText, String outputText, String sentence) {
        this.rowNum = rowNum;
        this.date = date;
        this.inputText = inputText;
        this.outputText = outputText;
        this.sentence = sentence;
    }

    // Getters and Setters
    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    // 추가적인 자막 데이터를 처리할 수 있는 메서드를 여기에 추가할 수 있습니다.
}