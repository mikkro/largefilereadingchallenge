package com.kyotu.largefilereadingchallenge.controller.dto;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime date, String message) {
}
