package com.example.demo.entity;

import java.time.Duration;

public record CreateTaskRequest(String name, String cmd) {
    public Task toTask() {
        return new Task(
            null,
            name,
            "",
            cmd,
            "",
            Task.CREATED,
            false,
            null,
            null,
            Duration.ZERO
        );
    }
}
