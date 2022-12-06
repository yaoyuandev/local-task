package com.example.demo.entity;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    String pid;

    String cmd;

    String output;

    String status;

    boolean killed;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    Duration time;
}
