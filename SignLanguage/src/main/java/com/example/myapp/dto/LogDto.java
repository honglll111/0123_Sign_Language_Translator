package com.example.myapp.dto;

import java.sql.Timestamp;

public class LogDto {

    private int rowNum;
    private Timestamp date;
    private String inputText; // 중간 단어들 (검정색으로 표시)
    private String outputText; // 최종 문장 (파란색으로 표시)

    public LogDto(int rowNum, Timestamp date, String inputText, String outputText) {
        this.rowNum = rowNum;
        this.date = date;
        this.inputText = inputText;
        this.outputText = outputText;
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
}
