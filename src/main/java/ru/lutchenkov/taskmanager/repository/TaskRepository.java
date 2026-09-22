package ru.lutchenkov.taskmanager.repository;

import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    long deleteByStatusAndCreatedAtBefore(TaskStatus status, LocalDateTime threshold);
}