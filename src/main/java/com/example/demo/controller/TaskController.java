package com.example.demo.controller;

import com.example.demo.entity.CreateTaskRequest;
import com.example.demo.entity.Tasks;
import com.example.demo.service.TaskService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public record TaskController(TaskService service) {
    @PostMapping("")
    void addTask(@RequestBody CreateTaskRequest task) {
        service.create(task);
    }

    @GetMapping("")
    Tasks tasks() {
        return service.tasks();
    }
    @DeleteMapping("/{id}")
    void deleteTask(@PathVariable Long id) {
        service.delete(id);
    }
    @PostMapping("/{id}/kill")
    void killTask(@PathVariable Long id) {
        service.kill(id);
    }
    @PostMapping("/{id}/retry")
    void retryTask(@PathVariable Long id) {
        service.retry(id);
    }

    @PostMapping("/{id}/cancel")
    void cancelTask(@PathVariable Long id) {
        service.cancel(id);
    }

    @GetMapping("/search")
    Tasks searchTasks(@RequestParam String q) {
        return service.search(q);
    }
}
