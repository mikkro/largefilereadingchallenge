package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.mapper.TaskMapper;
import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskService {

    final TaskRepository taskRepository;

    final TaskMapper taskMapper;

    public TaskService(final TaskRepository taskRepository,
                       final TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Task createTask(final String url) {
        final LocalDateTime now = LocalDateTime.now();
        final Task task =
                Task.builder()
                        .uri(url)
                        .createDate(now)
                        .lastUpdate(now)
                        .taskStatus(TaskStatus.PENDING)
                        .build();
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(final Task task, final TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        task.setLastUpdate(LocalDateTime.now());
        return taskRepository.save(task);
    }


    public List<TaskStatusResponse> getTasksStatus() {
        return taskRepository.findAllByOrderByCreateDateAsc().stream().map(taskMapper::map).toList();
    }
}
