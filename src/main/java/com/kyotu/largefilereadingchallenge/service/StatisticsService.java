package com.kyotu.largefilereadingchallenge.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.exception.StatisticsNotFoundException;
import com.kyotu.largefilereadingchallenge.mapper.StatisticsMapper;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final TaskService taskService;
    private final FileConfigurationService fileConfigurationService;
    private final FileChangeDetector fileChangeDetector;

    public StatisticsService(final StatisticsMapper statisticsMapper,
                             final TaskService taskService,
                             final FileConfigurationService fileConfigurationService,
                             final FileChangeDetector fileChangeDetector) {
        this.statisticsMapper = statisticsMapper;
        this.taskService = taskService;
        this.fileConfigurationService = fileConfigurationService;
        this.fileChangeDetector = fileChangeDetector;
    }

    public List<StatisticsResponse> getStatisticsForCity(final String city) throws JsonProcessingException {
        final FileConfiguration fileConfiguration = fileConfigurationService.getFileConfig();
        final Optional<Task> task = taskService.findFirstByFileConfigurationIdAndCity(fileConfiguration.getId(), city);
        final long newFileSize = new File(fileConfiguration.getPath()).length();

        if (task.isEmpty()) {
            final Task newTask = taskService.createTask(fileConfiguration, city, newFileSize);
            throw new StatisticsNotFoundException(createExceptionMessage(fileConfiguration.getPath(), city, newTask.getId()));
        }

        final Task currentTask = task.get();
        boolean hasFileChanged = fileChangeDetector.detect(currentTask.getFileSize(), newFileSize);
        if (hasFileChanged) {
            final Task fileChangedTask = taskService.createTask(fileConfiguration, currentTask.getCity(), newFileSize);
            throw new StatisticsNotFoundException(createExceptionMessage(fileConfiguration.getPath(), city, fileChangedTask.getId()));
        }

        if (currentTask.getTaskStatus() != TaskStatus.FINISHED) {
            throw new StatisticsNotFoundException(createExceptionMessage(fileConfiguration.getPath(), city, currentTask.getId()));
        }

        return handleFinishedTask(currentTask);
    }

    private List<StatisticsResponse> handleFinishedTask(final Task currentTask) {
        final Statistics statistics = currentTask.getStatistics();
        final Map<Integer, StatisticsDto> statisticsMap = statisticsMapper.mapStatistics(statistics);
        return statisticsMapper.mapTemperature(statisticsMap);
    }


    private String createExceptionMessage(final String filePath, final String city, final Long id) {
        return String.format("The file with path [%s] is being processed to collect statistics for city of [%s]. Try again later. You can monitor task with id: [%s].", filePath, city, id);
    }
}
