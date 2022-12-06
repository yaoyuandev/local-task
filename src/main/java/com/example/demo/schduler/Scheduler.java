package com.example.demo.schduler;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    final TaskService service;
    final ObjectMapper om;

    @Scheduled(fixedDelayString = "${jobs.rate:1000}")
    void run() {
        for (;;) {
            val taskOp = service.findTodo();
            if (!taskOp.isPresent()) {
                return;
            }
            val task = taskOp.get();
            task.setStatus(Task.RUNNING);
            service.update(task);
            val path = "logs/" + task.getId() + ".log";
            val outputFile = new File(path);
            if (!outputFile.getParentFile().isDirectory()) {
                val ok = outputFile.getParentFile().mkdirs();
            }
            try (val outputStream = new FileOutputStream(outputFile)) {
                var p = new ProcessBuilder("bash", "-c", task.getCmd()).start();
                val future = p.onExit();
                task.setPid("" + p.pid());
                service.update(task);
                p = future.get();
                task.setPid("");
                val durationOp = p.info().totalCpuDuration();
                task.setTime(durationOp.orElse(Duration.ZERO));
                if (p.exitValue() == 0) {
                    FileCopyUtils.copy(p.getInputStream(), outputStream);
                    task.setStatus(Task.COMPLETED);
                } else {
                    if (service.isKilled(task.getId())) {
                        task.setStatus(Task.KILLED);
                    } else {
                        task.setStatus(Task.FAILED);
                    }
                    FileCopyUtils.copy(p.getErrorStream(), outputStream);
                }
                task.setOutput(path);
            } catch (Exception e) {
                log.error("", e);
            }
            service.update(task);
        }
    }
}
