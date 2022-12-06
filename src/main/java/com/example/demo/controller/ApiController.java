package com.example.demo.controller;

import com.example.demo.entity.CreateTaskRequest;
import com.example.demo.entity.Tasks;
import com.example.demo.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public record ApiController(TaskService service) {
    @PostMapping("/tasks")
    void addTask(@RequestBody CreateTaskRequest task) {
        service.create(task);
    }

    @GetMapping("/tasks")
    Tasks tasks() {
        return service.tasks();
    }
}
