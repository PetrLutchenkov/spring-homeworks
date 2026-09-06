package ru.lutchenkov.taskmanager.exception;

public class TaskNotFoundException extends ApplicationException {
    public TaskNotFoundException(Long id) {
        super("Task with ID " + id + " not found", "TASK_NOT_FOUND");
    }
}