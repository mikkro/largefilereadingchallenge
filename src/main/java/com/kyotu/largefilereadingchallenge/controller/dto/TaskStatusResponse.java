package com.kyotu.largefilereadingchallenge.controller.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskStatusResponse(Long id, TaskStatus taskStatus, LocalDateTime lastUpdate, String path, String city) {
}
