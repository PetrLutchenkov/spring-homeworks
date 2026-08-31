package ru.lutchenkov.taskmanager.service;

import org.springframework.stereotype.Service;
import ru.lutchenkov.taskmanager.annotation.Auditable;
import ru.lutchenkov.taskmanager.exception.TaskNotFoundException;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final Map<Long, Task> taskRepository = new ConcurrentHashMap<>();

    // Потокобезопасный генератор уникальных ID (заменяет нам автоинкремент в БД)
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskRepository.values());
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    @Auditable
    public Task createTask(Task task) {
        Long newId = idGenerator.getAndIncrement();
        task.setId(newId);
        task.setCreatedAt(LocalDateTime.now());

        // Если клиент не передал статус, по умолчанию задача получает статус TODO
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        taskRepository.put(newId, task); // Сохраняем в нашу "базу"
        return task;
    }

    public Task updateTask(Long id, Task updatedTaskData) {
        // Если задачи нет — выбрасываем исключение
        if (!taskRepository.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }

        Task existingTask = taskRepository.get(id);

        // Обновляем только разрешенные поля (тут твой код не меняется)
        existingTask.setTitle(updatedTaskData.getTitle());
        existingTask.setDescription(updatedTaskData.getDescription());
        if (updatedTaskData.getStatus() != null) {
            existingTask.setStatus(updatedTaskData.getStatus());
        }

        // Возвращаем просто объект, без Optional
        return existingTask;
    }

    @Auditable
    public void deleteTask(Long id) {
        Task removedTask = taskRepository.remove(id);
        if (removedTask == null) {
            throw new TaskNotFoundException(id);
        }
    }
}