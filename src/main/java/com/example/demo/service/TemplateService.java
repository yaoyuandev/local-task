package com.example.demo.service;

import com.example.demo.entity.Template;
import com.example.demo.repository.TemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateService {

    final TemplateRepository templateRepository;
    final TaskService taskService;

    public Template addByTaskId(Long taskId) {
        val task = taskService.findById(taskId);
        val template = new Template();
        template.setCmd(task.getCmd());
        template.setInterpreter(task.getInterpreter());
        return templateRepository.save(template);
    }

    public List<Template> all() {
        return templateRepository.findAll();
    }

    public Template findById(Long id) {
        return templateRepository.findById(id).get();
    }
}
