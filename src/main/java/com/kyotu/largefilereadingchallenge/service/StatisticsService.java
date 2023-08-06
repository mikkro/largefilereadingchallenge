package com.kyotu.largefilereadingchallenge.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.exception.TaskNotFoundException;
import com.kyotu.largefilereadingchallenge.mapper.StatisticsMapper;
import com.kyotu.largefilereadingchallenge.repository.StatisticsRepository;
import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final StatisticsRepository statisticsRepository;
    private final ObjectMapper objectMapper;

    public StatisticsService(final StatisticsMapper statisticsMapper,
                             final StatisticsRepository statisticsRepository,
                             final ObjectMapper objectMapper) {
        this.statisticsMapper = statisticsMapper;
        this.statisticsRepository = statisticsRepository;
        this.objectMapper = objectMapper;
    }

    public List<StatisticsResponse> getStatisticsForTask(final Long taskId) throws JsonProcessingException {

        final Statistics statistics = Optional.ofNullable(statisticsRepository.findByTaskId(taskId))
                .orElseThrow(() -> new TaskNotFoundException(String.format("Statistics for task with id [%d] not found", taskId)));


        final Map<Integer, StatisticsDto> statisticsMap = objectMapper.readValue(statistics.getResult(), new TypeReference<>() {
        });
        return statisticsMapper.mapTemperature(statisticsMap);
    }


}
