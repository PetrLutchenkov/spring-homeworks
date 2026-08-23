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
        return ResponseEntity.ok(tasks); // Возвращает статус 200 OK
    }

    // 2. Получить задачу по ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        // Если Optional пустой - возвращаем 404 Not Found, иначе 200 OK
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(mapToDto(task)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 3. Создать новую задачу
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        // Маппинг: Создаем Entity из того, что прислал клиент
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        // Отдаем сервису на сохранение
        Task createdTask = taskService.createTask(task);

        // Возвращаем статус 201 Created и готовый DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdTask));
    }

    // 4. Обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        Task taskData = new Task();
        taskData.setTitle(request.getTitle());
        taskData.setDescription(request.getDescription());
        taskData.setStatus(request.getStatus());

        return taskService.updateTask(id, taskData)
                .map(updatedTask -> ResponseEntity.ok(mapToDto(updatedTask)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 5. Удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            // Если удалилось, возвращаем 204 No Content (успех, но тела ответа нет)
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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