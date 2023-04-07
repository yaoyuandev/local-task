package com.example.demo.service;

import com.example.demo.entity.CreateTaskRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.Tasks;
import com.example.demo.repository.TaskRepository;
import java.util.Optional;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public record TaskService(TaskRepository repository) {
    public void create(CreateTaskRequest createTaskRequest) {
        repository.save(createTaskRequest.toTask());
    }

    public Tasks tasks() {
        return new Tasks(repository.findByOrderByIdDesc());
    }

    public Optional<Task> findTodo() {
        return repository.findFirstByStatus(Task.CREATED);
    }

    public boolean existsTodo() {
        return repository.existsByStatus(Task.CREATED);
    }

    public void update(Task task) {
        repository.save(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void retry(Long id) {
        val taskOp = repository.findById(id);
        if (taskOp.isEmpty()) {
            return;
        }
        val task = taskOp.get();
        update(task);
        val createTaskRequest = new CreateTaskRequest(
            task.getName(),
            task.getInterpreter(),
            task.getCmd()
        );
        create(createTaskRequest);
    }

    public void kill(Long id) {
        val taskOp = repository.findById(id);
        if (taskOp.isEmpty()) {
            return;
        }
        val task = taskOp.get();
        if (ObjectUtils.isEmpty(task.getPid())) {
            return;
        }
        val p = ProcessHandle.of(Long.parseLong(task.getPid()));
        task.setKilled(true);
        update(task);
        p.ifPresent(ProcessHandle::destroy);
    }

    public boolean isKilled(Long id) {
        val taskOp = repository.findById(id);
        if (taskOp.isEmpty()) {
            return false;
        }
        return taskOp.get().isKilled();
    }

    public Task findById(Long id) {
        return repository.findById(id).get();
    }

    public void cancel(Long id) {
        repository
            .findById(id)
            .ifPresent(it -> {
                it.setStatus(Task.CANCELED);
                repository.save(it);
            });
    }

    public Tasks search(String q) {
        return new Tasks(repository.findByCmdContainsIgnoreCase(q));
    }
}
