package com.example.demo.controller;

import com.example.demo.entity.CreateTaskRequest;
import com.example.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public record IndexController(TaskService service) {
    @PostMapping("/run")
    String run(@ModelAttribute CreateTaskRequest task, Model model) {
        service.create(task);
        return list(model);
    }

    @GetMapping("/tasks")
    String list(Model model) {
        model.addAttribute("tasks", service.tasks().items());
        return "list";
    }

    @DeleteMapping("/tasks/{id}")
    String delete(@PathVariable Long id, Model model) {
        service.delete(id);
        return list(model);
    }

    @PostMapping("/tasks/{id}/retry")
    String retry(@PathVariable Long id, Model model) {
        service.retry(id);
        return list(model);
    }

    @PostMapping("/tasks/{id}/kill")
    String kill(@PathVariable Long id, Model model) {
        service.kill(id);
        return list(model);
    }
}
