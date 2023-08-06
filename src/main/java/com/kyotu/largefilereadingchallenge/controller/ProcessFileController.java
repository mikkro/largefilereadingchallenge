package com.kyotu.largefilereadingchallenge.controller;


import com.kyotu.largefilereadingchallenge.controller.dto.ProcessFileRequest;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/temperatures")
@Tag(description = "To interact with statistics.", name = "Statistics")
public class ProcessFileController {

    private final TaskService taskService;
    private final String applicationBaseUri;

    public ProcessFileController(final TaskService taskService,
                                 @Value("${application.base-uri}") final String applicationBaseUri) {
        this.taskService = taskService;
        this.applicationBaseUri = applicationBaseUri;
    }


    @PostMapping
    @Operation(description = "Create a background task to process file from given URI.")
    public ResponseEntity<Void> processFile(@RequestBody @Valid final ProcessFileRequest fileUri) {
        final Task task = taskService.createTask(fileUri.fileUri());

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Resource-Id",
                String.valueOf(task.getId()));
        final URI location = URI.create(String.format("%s/tasks/%d/statistics", applicationBaseUri, task.getId()));

        return ResponseEntity
                .accepted()
                .location(location)
                .headers(responseHeaders)
                .build();
    }
}
