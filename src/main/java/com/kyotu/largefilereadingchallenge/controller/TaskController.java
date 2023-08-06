package com.kyotu.largefilereadingchallenge.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.service.StatisticsService;
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
    private final StatisticsService statisticsService;

    public TaskController(final TaskService taskService,
                          final StatisticsService statisticsService) {
        this.taskService = taskService;
        this.statisticsService = statisticsService;
    }


    @GetMapping("/{taskId}/statistics")
    @Operation(description = "Get task details.")
    public List<StatisticsResponse> getStatisticsForTask(@PathVariable final Long taskId) throws JsonProcessingException {
        return statisticsService.getStatisticsForTask(taskId);
    }

    @GetMapping
    @Operation(description = "Get task statuses.")
    public List<TaskStatusResponse> getTasks() {
        return taskService.getTasksStatus();
    }
}
