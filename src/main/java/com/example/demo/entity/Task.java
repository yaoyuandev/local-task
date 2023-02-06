package com.example.demo.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Task {

    public static final String CREATED = "created";
    public static final String RUNNING = "running";
    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";
    public static final String KILLED = "killed";

    public static final String BASH = "bash";

    public static final String JUPYTER = "jupyter";

    public static final String PYTHON = "python";

    public static final String ZX = "zx";

    static final String IPYTHON = "ipython";

    static final int CMD_LEN = 10000;

    static final Map<String, String> BADGE_MAP = Map.of(
        CREATED,
        "secondary",
        RUNNING,
        "primary",
        COMPLETED,
        "success",
        FAILED,
        "danger",
        KILLED,
        "warning"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    String name;

    String pid;

    String interpreter;

    @Column(length = CMD_LEN)
    String cmd;

    String output;

    String status;

    boolean killed;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    Duration time;

    LocalDateTime createdAtSecond() {
        return createdAt.truncatedTo(ChronoUnit.SECONDS);
    }

    LocalDateTime updatedAtSecond() {
        return updatedAt.truncatedTo(ChronoUnit.SECONDS);
    }

    String timeStr() {
        return time.toString().substring(2);
    }

    String badge() {
        return BADGE_MAP.get(status);
    }

    public String outputUrl() {
        if (output.endsWith(".html")) {
            return "/api/logs?file=" + output;
        }
        return "logs?file=" + output;
    }

    public boolean isBash() {
        return BASH.equals(interpreter);
    }

    public boolean isJupyter() {
        return JUPYTER.equals(interpreter);
    }

    public boolean isIpython() {
        return IPYTHON.equals(interpreter);
    }
}
