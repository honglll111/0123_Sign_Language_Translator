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
        List<Log> logs = logRepository.findAllLogsOrderedByDateDesc(); // DB에서 내림차순으로 정렬된 로그를 가져옴
        model.addAttribute("logs", logs);
        return "records";
    }
}
