package com.example.myapp.controller;

import com.example.myapp.model.Log;
import com.example.myapp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecordsController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping("/records")
    public String showProjects(Model model) {
        List<com.example.myapp.model.Log> logs = logRepository.findAll(); // DB에서 모든 로그를 가져옴
        model.addAttribute("logs", logs);
        return "records";
    }
}
