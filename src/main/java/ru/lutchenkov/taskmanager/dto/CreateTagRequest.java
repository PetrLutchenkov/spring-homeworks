package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTagRequest {

    @NotBlank(message = "Название тега не может быть пустым")
    private String name;
}