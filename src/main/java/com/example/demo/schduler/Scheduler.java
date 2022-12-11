package com.example.demo.schduler;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
        while (service.existsTodo()) {
            internalRun();
        }
    }

    void internalRun() {
        val taskOp = service.findTodo();
        if (!taskOp.isPresent()) {
            return;
        }
        val task = taskOp.get();
        task.setStatus(Task.RUNNING);
        service.update(task);
        if (task.isBash()) {
            runBashTask(task);
        }
        if (task.isIpython()) {
            runIpython(task);
        }
    }

    File createLogfile(Task task) {
        val outputFile = new File(task.getOutput());
        if (!outputFile.getParentFile().isDirectory()) {
            val ok = outputFile.getParentFile().mkdirs();
        }
        return outputFile;
    }

    @SneakyThrows
    void runBashTask(Task task) {
        task.setOutput("logs/" + task.getId() + "-" + task.getName() + ".log");
        createLogfile(task);
        var pb = new ProcessBuilder("bash", "-c", task.getCmd());
        runProcess(task, pb, true);
    }

    @SneakyThrows
    void runIpython(Task task) {
        val taskFileName = task.getId() + "-" + task.getName();
        val pythonFile = new File("tmp/" + taskFileName + ".py");
        if (!pythonFile.getParentFile().isDirectory()) {
            val ok = pythonFile.getParentFile().mkdirs();
        }
        FileCopyUtils.copy(task.getCmd().getBytes(), pythonFile);
        task.setOutput("logs/" + task.getId() + "-" + task.getName() + ".html");
        val cmd =
            "p2j " +
            pythonFile +
            "&& jupyter nbconvert  --execute  --to html " +
            " tmp/" +
            taskFileName +
            ".ipynb --output ../logs/" +
            taskFileName +
            ".html";
        log.info("run {}", cmd);
        val p = new ProcessBuilder("bash", "-c", cmd);
        runProcess(task, p, false);
    }

    @SneakyThrows
    void runProcess(Task task, ProcessBuilder pb, boolean saveOutput) {
        var p = pb.start();
        val future = p.onExit();
        task.setPid("" + p.pid());
        service.update(task);
        val start = LocalDateTime.now();
        p = future.get();
        task.setPid("");
        val end = LocalDateTime.now();
        task.setTime(Duration.between(start, end));
        if (p.exitValue() == 0) {
            if (saveOutput) {
                try (
                    val outputStream = new FileOutputStream(task.getOutput())
                ) {
                    FileCopyUtils.copy(p.getInputStream(), outputStream);
                }
            }
            task.setStatus(Task.COMPLETED);
        } else {
            if (service.isKilled(task.getId())) {
                task.setStatus(Task.KILLED);
            } else {
                task.setStatus(Task.FAILED);
            }
            try (val outputStream = new FileOutputStream(task.getOutput())) {
                FileCopyUtils.copy(p.getErrorStream(), outputStream);
            }
        }
        service.update(task);
    }
}
