package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.exception.TaskNotFoundException;
import com.kyotu.largefilereadingchallenge.mapper.TaskMapper;
import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TaskService {

    final TaskRepository taskRepository;

    final TaskMapper taskMapper;

    public TaskService(final TaskRepository taskRepository,
                       final TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Task createTask(final FileConfiguration fileConfiguration, final String city, final Long fileSize) {
        final LocalDateTime now = LocalDateTime.now();
        final Task task =
                Task.builder()
                        .fileConfiguration(fileConfiguration)
                        .city(city)
                        .createDate(now)
                        .lastUpdate(now)
                        .taskStatus(TaskStatus.PENDING)
                        .fileSize(fileSize)
                        .build();
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(final Task task, final TaskStatus taskStatus) {
        task.setTaskStatus(taskStatus);
        task.setLastUpdate(LocalDateTime.now());
        return taskRepository.save(task);
    }


    public List<TaskStatusResponse> getTasksStatus() {
        return taskRepository.findAllByOrderByCreateDateAsc().stream().map(taskMapper::mapTaskStatus).toList();
    }

    public Optional<Task> findFirstByFileConfigurationIdAndCity(final Long id, final String city) {
        return taskRepository.findFirstByFileConfigurationIdAndCityOrderByCreateDateDesc(id, city);
    }

    public TaskStatusResponse getTaskStatus(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id [%d] not found", id)));
        return taskMapper.mapTaskStatus(task);
    }
}
