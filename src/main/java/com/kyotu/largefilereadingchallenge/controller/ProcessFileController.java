package com.kyotu.largefilereadingchallenge.controller;


import com.kyotu.largefilereadingchallenge.controller.dto.ProcessFileRequest;
import com.kyotu.largefilereadingchallenge.service.FileConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration")
@Tag(description = "To interact with file configuration.", name = "Configuration")
public class ProcessFileController {
    private final FileConfigurationService fileConfigurationService;

    public ProcessFileController(final FileConfigurationService fileConfigurationService) {
        this.fileConfigurationService = fileConfigurationService;
    }

    @PostMapping
    @Operation(description = "Set file path to be processed.")
    public ResponseEntity<Void> processFile(@RequestBody @Valid final ProcessFileRequest request) {
        fileConfigurationService.setFilePath(request.filePath());
        return ResponseEntity.ok().build();
    }
}
