package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTagRequest {

    @NotBlank(message = "Название тега не может быть пустым")
    private String name;
}