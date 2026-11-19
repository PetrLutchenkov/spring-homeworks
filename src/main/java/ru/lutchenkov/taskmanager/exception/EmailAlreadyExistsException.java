package ru.lutchenkov.taskmanager.exception;

public class EmailAlreadyExistsException extends ApplicationException {
    public EmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists", "EMAIL_ALREADY_EXISTS");
    }
}
