package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitService implements CommandLineRunner {

    final TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info(
            "reset status {}",
            taskRepository.updateStatusAndKilledByStatus(
                Task.KILLED,
                true,
                Task.RUNNING
            )
        );
    }
}
