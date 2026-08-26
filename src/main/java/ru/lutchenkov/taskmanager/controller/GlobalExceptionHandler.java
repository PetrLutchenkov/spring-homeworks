package ru.lutchenkov.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.lutchenkov.taskmanager.dto.ErrorResponse;
import ru.lutchenkov.taskmanager.exception.TaskNotFoundException;

import jakarta.servlet.http.HttpServletRequest; // Важно: в Spring Boot 3 импорт из jakarta!
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Обработка отсутствия задачи (404)
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 2. Обработка ошибок валидации (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Достаем Акт досмотра (BindingResult) и собираем список проблемных полей
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponse response = new ErrorResponse(
                "Ошибка валидации запроса",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        response.setValidationErrors(errors); // Записываем список ошибок

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 3. Страховочный обработчик для всех остальных ошибок (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                "Внутренняя ошибка сервера",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );
        // В реальном проекте тут должен быть еще логгер: log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}