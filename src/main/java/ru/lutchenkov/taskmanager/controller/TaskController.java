package ru.lutchenkov.taskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lutchenkov.taskmanager.annotation.RateLimit;
import ru.lutchenkov.taskmanager.dto.CreateTaskRequest;
import ru.lutchenkov.taskmanager.dto.TaskDto;
import ru.lutchenkov.taskmanager.dto.UpdateTaskRequest;
import ru.lutchenkov.taskmanager.model.Tag;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @RateLimit(key = "get_all_tasks_limit", requestsPerMinute = 100)
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks().stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(mapToDto(task));
    }

    @RateLimit(key = "create_task_limit", requestsPerMinute = 10)
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        Task taskData = new Task();
        taskData.setTitle(request.getTitle());
        taskData.setDescription(request.getDescription());
        taskData.setStatus(request.getStatus());

        Task updatedTask = taskService.updateTask(id, taskData);
        return ResponseEntity.ok(mapToDto(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    private TaskDto mapToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());

        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
            dto.setAssigneeName(task.getAssignee().getUsername());
        }

        if (task.getTags() != null) {
            dto.setTags(task.getTags().stream()
                    .map(Tag::getName)
                    .toList());
        }

        return dto;
    }
}