package com.kyotu.largefilereadingchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyotu.largefilereadingchallenge.mapper.StatisticsMapper;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ImportService {

    private final StatisticsMapper statisticsMapper;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final FileConfigurationService fileConfigurationService;

    public ImportService(final StatisticsMapper statisticsMapper,
                         final TaskService taskService,
                         final ObjectMapper objectMapper,
                         final FileConfigurationService fileConfigurationService) {
        this.statisticsMapper = statisticsMapper;
        this.taskService = taskService;
        this.objectMapper = objectMapper;
        this.fileConfigurationService = fileConfigurationService;
    }

    public void startImport(final Task task) {
        log.info("Started an import task with id {}.", task.getId());
        final Task savedTask = taskService.updateTaskStatus(task, TaskStatus.IN_PROGRESS);
        final FileConfiguration fileConfiguration = fileConfigurationService.getFileConfig();

        final Map<Integer, StatisticsDto> map = new HashMap<>();

        try {
            final File file = new File(fileConfiguration.getPath());
            try (
                    final InputStream inputStream = new FileInputStream(file);
                    final Reader reader = new InputStreamReader(inputStream);
                    final BufferedReader bufferedReader = new BufferedReader(reader)
            ) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String[] text = line.split(";");
                    final String city = text[0];
                    if (task.getCity().equals(city)) {
                        final Integer year = statisticsMapper.mapDate(text[1]).getYear();
                        final float temperature = Float.parseFloat(text[2]);
                        map.computeIfAbsent(year, key -> new StatisticsDto(0, 0.0))
                                .addTemperature(temperature);
                    }
                }

                savedTask.setStatistics(createStatistics(savedTask, map));
                savedTask.setFileSize(file.length());
                taskService.updateTaskStatus(savedTask, TaskStatus.FINISHED);
                log.info("Finished task with id {}.", savedTask.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            taskService.updateTaskStatus(savedTask, TaskStatus.FAILED);
        }
    }

    private Statistics createStatistics(final Task task, final Map<Integer, StatisticsDto> map) throws JsonProcessingException {
        return Statistics.builder()
                .lastUpdate(LocalDateTime.now())
                .task(task)
                .result(objectMapper.writeValueAsString(map))
                .build();
    }

}
