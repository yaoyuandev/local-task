package com.example.demo.entity;

import java.time.Duration;

public record CreateTaskRequest(String name, String interpreter, String cmd) {
    public Task toTask() {
        return new Task(
            null,
            name,
            "",
            interpreter,
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
