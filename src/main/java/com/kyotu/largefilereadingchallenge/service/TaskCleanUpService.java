package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.repository.StatisticsRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCleanUpService {

    private final TaskRepository taskRepository;

    private final StatisticsRepository statisticsRepository;


    public TaskCleanUpService(final TaskRepository taskRepository,
                              final StatisticsRepository statisticsRepository) {
        this.taskRepository = taskRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Transactional
    public void cleanUpTasks(final List<Task> tasks) {
        statisticsRepository.deleteStatisticsByTaskIdIn(tasks.stream().map(Task::getId).toList());
        taskRepository.deleteAll(tasks);
    }
}
