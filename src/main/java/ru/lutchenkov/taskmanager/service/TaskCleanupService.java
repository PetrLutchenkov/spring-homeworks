package ru.lutchenkov.taskmanager.service;

import ru.lutchenkov.taskmanager.model.TaskStatus;
import ru.lutchenkov.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j

public class TaskCleanupService {

    private final TaskRepository taskRepository;

    @Scheduled(cron = "${app.task.cleanup.cron}")
    public void cleanUpOldCompletedTasks() {
        log.info("Запуск фоновой задачи: очистка старых завершённых задач...");
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
        long deletedCount = taskRepository.deleteByStatusAndCreatedAtBefore(
                TaskStatus.DONE,
                thresholdDate
        );
        if (deletedCount > 0) {
            log.info("Очистка завершена успешно. Удалено задач: {}", deletedCount);
        } else {
            log.info("Задач для удаления не найдено");
        }
    }
}
