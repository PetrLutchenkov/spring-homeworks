package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.lutchenkov.taskmanager.model.TaskStatus;

@Data
public class UpdateTaskRequest {

    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String title;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    private TaskStatus status;
}