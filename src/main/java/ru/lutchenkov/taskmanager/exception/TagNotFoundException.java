package ru.lutchenkov.taskmanager.exception;

public class TagNotFoundException extends ApplicationException {
    public TagNotFoundException(Long id) {
        super("Tag with ID " + id + " not found", "TAG_NOT_FOUND");
    }
}