package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCommentRequest {

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}