package com.example.myapp.service;

import com.example.myapp.model.Caption;
import com.example.myapp.repository.CaptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaptionService {

    private final CaptionRepository captionRepository;

    public CaptionService(CaptionRepository captionRepository) {
        this.captionRepository = captionRepository;
    }

    public void saveCaption(String inputText, String outputText) {
        Caption caption = new Caption();
        caption.setInputText(inputText);
        caption.setOutputText(outputText);
        caption.setTimestamp(LocalDateTime.now());

        captionRepository.save(caption);
    }
}
