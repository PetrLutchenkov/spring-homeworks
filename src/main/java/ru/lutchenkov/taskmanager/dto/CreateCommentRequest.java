package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

    @NotNull
    private Long taskId;

    @NotNull
    private Long authorId;
}