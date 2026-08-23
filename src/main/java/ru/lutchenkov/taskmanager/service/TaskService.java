package ru.lutchenkov.taskmanager.service;

import org.springframework.stereotype.Service;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Optional<Task> getTaskById(Long id) {
        // Используем Optional, чтобы красиво сообщить контроллеру, что задачи нет (защита от NullPointerException)
        return Optional.ofNullable(taskRepository.get(id));
    }

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

    public Optional<Task> updateTask(Long id, Task updatedTaskData) {
        if (!taskRepository.containsKey(id)) {
            return Optional.empty();
        }
        Task existingTask = taskRepository.get(id);

        // Обновляем только разрешенные поля
        existingTask.setTitle(updatedTaskData.getTitle());
        existingTask.setDescription(updatedTaskData.getDescription());
        if (updatedTaskData.getStatus() != null) {
            existingTask.setStatus(updatedTaskData.getStatus());
        }

        return Optional.of(existingTask);
    }

    public boolean deleteTask(Long id) {
        // Метод remove возвращает удаленный объект, если он был, или null, если такого ключа нет.
        // Поэтому мы просто проверяем, не null ли вернулся.
        return taskRepository.remove(id) != null;
    }
}