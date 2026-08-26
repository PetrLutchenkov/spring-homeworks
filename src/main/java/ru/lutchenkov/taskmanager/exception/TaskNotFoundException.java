package ru.lutchenkov.taskmanager.exception;

public class TaskNotFoundException extends ApplicationException {
    public TaskNotFoundException(Long id) {
        // Передаем текст для логов и машиночитаемый код для ТЗ
        super("Task with ID " + id + " not found", "TASK_NOT_FOUND");
    }
}