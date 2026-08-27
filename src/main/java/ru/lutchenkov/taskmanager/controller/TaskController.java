package ru.lutchenkov.taskmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lutchenkov.taskmanager.dto.CreateTaskRequest;
import ru.lutchenkov.taskmanager.dto.TaskDto;
import ru.lutchenkov.taskmanager.dto.UpdateTaskRequest;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 1. Получить все задачи
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        // Достаем сущности из сервиса, превращаем каждую в DTO и собираем в список
        List<TaskDto> tasks = taskService.getAllTasks().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id); // Если задачи нет, отсюда вылетит ошибка в Главврача
        return ResponseEntity.ok(mapToDto(task));
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        // Маппинг: Создаем Entity из того, что прислал клиент
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        // Отдаем сервису на сохранение
        Task createdTask = taskService.createTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdTask));
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        Task taskData = new Task();
        taskData.setTitle(request.getTitle());
        taskData.setDescription(request.getDescription());
        taskData.setStatus(request.getStatus());

        return taskService.updateTask(id, taskData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // Просто вызываем удаление.
        // Если задачи нет, отсюда вылетит ошибка, и выполнение метода прервется.
        taskService.deleteTask(id);

        // Если мы дошли до этой строчки, значит ошибка не вылетела и задача успешно удалена!
        return ResponseEntity.noContent().build();
    }

    // =======================================================
    // Вспомогательный метод (Маппер) для превращения Entity в DTO
    // =======================================================
    private TaskDto mapToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        return dto;
    }
}