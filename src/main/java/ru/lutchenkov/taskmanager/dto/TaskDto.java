package ru.lutchenkov.taskmanager.dto;

import lombok.Data;
import ru.lutchenkov.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
}