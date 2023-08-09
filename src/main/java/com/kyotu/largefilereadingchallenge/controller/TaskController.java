package com.kyotu.largefilereadingchallenge.controller;


import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(description = "To interact with tasks.", name = "Tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(description = "Get task statuses.")
    public List<TaskStatusResponse> getTasks() {
        return taskService.getTasksStatus();
    }

    @GetMapping(path = "{id}")
    @Operation(description = "Get task status.")
    public TaskStatusResponse getTask(@PathVariable Long id) {
        return taskService.getTaskStatus(id);
    }
}
