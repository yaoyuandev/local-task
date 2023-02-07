package com.example.demo.repository;

import com.example.demo.entity.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying
    @Query("update Task t set t.status = ?1, t.killed = ?2 where t.status = ?3")
    int updateStatusAndKilledByStatus(
        String status,
        boolean killed,
        String status1
    );

    Optional<Task> findFirstByStatus(String status);

    List<Task> findByOrderByIdDesc();

    boolean existsByStatus(String status);
}
