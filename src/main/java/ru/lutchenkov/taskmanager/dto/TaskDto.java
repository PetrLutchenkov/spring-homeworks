package ru.lutchenkov.taskmanager.dto;

import lombok.Data;
import ru.lutchenkov.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private Long assigneeId;
    private String assigneeName;
    private List<String> tags;
}