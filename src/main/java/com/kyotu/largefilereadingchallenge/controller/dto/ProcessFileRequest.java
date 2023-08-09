package com.kyotu.largefilereadingchallenge.controller.dto;

import jakarta.validation.constraints.NotNull;

public record ProcessFileRequest(@NotNull String filePath) {
}
