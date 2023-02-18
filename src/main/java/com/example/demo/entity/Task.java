package com.example.demo.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
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
import lombok.val;
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

    public static final String CANCELED = "canceled";

    public static final String RETRIED = "retried";

    public static final String BASH = "bash";

    public static final String JUPYTER = "jupyter";

    public static final String PYTHON = "python";

    public static final String ZX = "zx";

    static final String IPYTHON = "ipython";

    static final int CMD_LEN = 10000;

    static final String CSS_WARNING = "warning";

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
        CSS_WARNING,
        CANCELED,
        CSS_WARNING,
        RETRIED,
        "warning"
    );

    static final String CSS_NONE = "d-none";
    static final String CSS_BUTTON_WARNING = "btn btn-warning";

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

    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    Duration time;

    LocalDateTime createdAtSecond() {
        if (createdAt == null) {
            return null;
        }
        return createdAt.truncatedTo(ChronoUnit.SECONDS);
    }

    String createdAtSecondStr() {
        val createAtSecond = createdAtSecond();
        if (createAtSecond == null) {
            return "";
        }
        return createAtSecond.toString();
    }

    LocalDateTime updatedAtSecond() {
        return updatedAt.truncatedTo(ChronoUnit.SECONDS);
    }

    Duration newTime() {
        if (isRunning() && time.isZero()) {
            return Duration.between(createdAt, LocalDateTime.now());
        }
        return time;
    }

    String timeStr() {
        return newTime().toString().substring(2);
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

    public String disabledDelete() {
        if (Task.RUNNING.equals(status)) {
            return "disabled";
        }
        return "";
    }

    boolean isRunning() {
        return RUNNING.equals(status);
    }

    String killButtonClass() {
        if (!isRunning()) {
            return CSS_NONE;
        }
        return CSS_BUTTON_WARNING;
    }

    boolean isCreated() {
        return CREATED.equals(status);
    }

    String cancelButtonClass() {
        if (isCreated()) {
            return CSS_BUTTON_WARNING;
        }
        return CSS_NONE;
    }
}
