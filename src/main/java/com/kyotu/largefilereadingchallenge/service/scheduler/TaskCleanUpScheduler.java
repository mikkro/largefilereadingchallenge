package com.kyotu.largefilereadingchallenge.service.scheduler;

import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.TaskCleanUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TaskCleanUpScheduler {

    private final TaskRepository taskRepository;
    private final TaskCleanUpService taskCleanUpService;

    public TaskCleanUpScheduler(final TaskRepository taskRepository,
                                final TaskCleanUpService taskCleanUpService) {
        this.taskRepository = taskRepository;
        this.taskCleanUpService = taskCleanUpService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleClean() {
        log.debug("Cleaning up successfully processed tasks.");
        final List<Task> tasks = taskRepository.findAllByTaskStatus(TaskStatus.FINISHED);
        taskCleanUpService.cleanUpTasks(tasks);
        log.debug("Cleaned up {} tasks.", tasks.size());
    }
}
