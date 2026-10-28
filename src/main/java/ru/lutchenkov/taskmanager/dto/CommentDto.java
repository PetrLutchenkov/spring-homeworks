package ru.lutchenkov.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long taskId;
    private Long authorId;
    private String authorName;
}