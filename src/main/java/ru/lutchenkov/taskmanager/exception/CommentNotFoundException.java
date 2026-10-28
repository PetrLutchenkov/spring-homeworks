package ru.lutchenkov.taskmanager.exception;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException(Long id) {
        super("Comment with ID " + id + " not found", "COMMENT_NOT_FOUND");
    }
}