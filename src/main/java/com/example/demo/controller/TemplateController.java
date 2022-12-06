package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/templates")
public class TemplateController {

    @GetMapping("{name}")
    String template(@PathVariable String name) {
        return name;
    }

    @GetMapping("/form")
    String form(Model model) {
        model.addAttribute("args", "[\"-c\", \"echo hello\"]");
        return "form";
    }
}
