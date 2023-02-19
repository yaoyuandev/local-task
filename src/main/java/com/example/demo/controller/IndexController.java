package com.example.demo.controller;

import com.example.demo.entity.CreateTaskRequest;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    final TaskService service;

    @Value("${cmd.min-width}")
    final int minWidth;

    @GetMapping("")
    String index(Model model) {
        return "index";
    }

    @PostMapping("/run")
    String run(@ModelAttribute CreateTaskRequest task, Model model) {
        service.create(task);
        return list(model);
    }

    @GetMapping("/tasks")
    String list(Model model) {
        model.addAttribute("tasks", service.tasks().items());
        model.addAttribute("minWidth", minWidth);
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

    @ResponseBody
    @PostMapping("/tasks/{id}/clone")
    String clone(@PathVariable Long id) {
        return service.findById(id).getCmd();
    }

    @PostMapping("/tasks/{id}/cancel")
    String cancel(@PathVariable Long id, Model model) {
        log.info("call cancel id = {}", id);
        service.cancel(id);
        return list(model);
    }
}
