package com.kyotu.largefilereadingchallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@Tag(description = "To interact with statistics.", name = "Statistics")

public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{city}")
    @Operation(description = "Get statistics for given City.")
    public List<StatisticsResponse> getStatisticsForTask(@PathVariable final String city) throws JsonProcessingException {
        return statisticsService.getStatisticsForCity(city);
    }
}
