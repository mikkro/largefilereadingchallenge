package com.kyotu.largefilereadingchallenge.service.scheduler;

import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ImportScheduler {
    private final TaskRepository taskRepository;
    private final ImportService importService;

    public ImportScheduler(final TaskRepository taskRepository,
                           final ImportService importService) {
        this.taskRepository = taskRepository;
        this.importService = importService;
    }

    @Scheduled(fixedDelay = 3000)
    public void scheduleFixedDelayTask() {
        final List<Task> tasks = taskRepository.findFirst100ByTaskStatusOrderByCreateDateAsc(TaskStatus.PENDING);
        log.debug("Starting processing of {} tasks.", tasks.size());
        tasks.forEach(importService::startImport);
    }
}
