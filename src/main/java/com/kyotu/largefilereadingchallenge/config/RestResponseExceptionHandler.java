package com.kyotu.largefilereadingchallenge.config;

import com.kyotu.largefilereadingchallenge.controller.dto.ErrorResponse;
import com.kyotu.largefilereadingchallenge.exception.FileConfigurationNotFoundException;
import com.kyotu.largefilereadingchallenge.exception.StatisticsNotFoundException;
import com.kyotu.largefilereadingchallenge.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class, FileConfigurationNotFoundException.class, StatisticsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}