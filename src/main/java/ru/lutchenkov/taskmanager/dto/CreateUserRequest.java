package ru.lutchenkov.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Неверный формат почты")
    private String email;
}