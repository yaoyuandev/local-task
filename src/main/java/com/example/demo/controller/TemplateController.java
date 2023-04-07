package com.example.demo.controller;

import com.example.demo.entity.Template;
import com.example.demo.service.TemplateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/templates")
public class TemplateController {

    final TemplateService templateService;

    @PostMapping("")
    Template addTemplateByTaskId(@RequestParam Long taskId) {
        return templateService.addByTaskId(taskId);
    }

    @GetMapping("")
    List<Template> templateList() {
        return templateService.all();
    }

    @GetMapping("/{id}")
    Template templateById(@PathVariable Long id) {
        return templateService.findById(id);
    }
}
