package com.kyotu.largefilereadingchallenge.mapper;

import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskStatusResponse map(final Task task){
        return new TaskStatusResponse(task.getId(), task.getTaskStatus(), task.getLastUpdate(), task.getMessage());
    }
}
