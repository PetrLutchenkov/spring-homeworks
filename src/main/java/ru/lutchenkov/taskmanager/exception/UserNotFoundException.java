package ru.lutchenkov.taskmanager.exception;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " not found", "USER_NOT_FOUND");
    }
}