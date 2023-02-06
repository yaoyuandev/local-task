package com.example.demo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/logs")
public class LogController {

    @GetMapping("")
    String log(@RequestParam String file, Model model) throws IOException {
        model.addAttribute("file", file);
        return "log";
    }
}
