package com.example.myapp.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "LOG")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROW_NUM")
    private Long rowNum;

    @Column(name = "LOG_DATE", nullable = false)
    private Timestamp logDate;

    @Lob
    @Column(name = "INPUT_TEXT", columnDefinition = "CLOB")
    private String inputText;

    @Lob
    @Column(name = "OUTPUT_TEXT", columnDefinition = "CLOB")
    private String outputText;

    @Lob
    @Column(name = "SENTENCE", columnDefinition = "CLOB", nullable = false)
    private String sentence;

    // Getter and Setter methods
    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public Timestamp getLogDate() {
        return logDate;
    }

    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
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

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return logDate != null ? logDate.toLocalDateTime().format(formatter) : "";
    }
}
