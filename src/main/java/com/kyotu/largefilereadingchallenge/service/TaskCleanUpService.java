package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCleanUpService {

    private final TaskRepository taskRepository;

    public TaskCleanUpService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void cleanUpTasks(final List<Task> tasks) {
        taskRepository.deleteAll(tasks);
    }
}
