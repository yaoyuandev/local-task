package com.example.demo.entity;

import java.time.Duration;

public record CreateTaskRequest(String cmd) {
    public Task toTask() {
        return new Task(
            null,
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
