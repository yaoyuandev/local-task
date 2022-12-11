package com.example.demo.repository;

import com.example.demo.entity.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findFirstByStatus(String status);

    List<Task> findByOrderByIdDesc();

    boolean existsByStatus(String status);
}
