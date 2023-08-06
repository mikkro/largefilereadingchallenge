package com.kyotu.largefilereadingchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyotu.largefilereadingchallenge.mapper.StatisticsMapper;
import com.kyotu.largefilereadingchallenge.repository.StatisticsRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ImportService {

    private final StatisticsMapper statisticsMapper;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final StatisticsRepository statisticsRepository;

    private final FileChangeDetector fileChangeDetector;

    public ImportService(final StatisticsMapper statisticsMapper,
                         final TaskService taskService,
                         final ObjectMapper objectMapper,
                         final StatisticsRepository statisticsRepository,
                         final FileChangeDetector fileChangeDetector) {
        this.statisticsMapper = statisticsMapper;
        this.taskService = taskService;
        this.objectMapper = objectMapper;
        this.statisticsRepository = statisticsRepository;
        this.fileChangeDetector = fileChangeDetector;
    }

    public void startImport(final Task task) {
        log.info("Started an import task with id {}.", task.getId());
        final Task savedTask = taskService.updateTaskStatus(task, TaskStatus.IN_PROGRESS);

        final Map<Integer, StatisticsDto> map = new HashMap<>();

        try {
            final URL url = new URL(task.getUri());
            try (
                    InputStream inputStream = new URL(task.getUri()).openStream();
                    Reader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader)
            ) {
                long fileSizeBefore = getFileSize(url);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] text = line.split(";");

                    final Integer year = statisticsMapper.mapDate(text[1]).getYear();
                    final float temperature = Float.parseFloat(text[2]);

                    map.computeIfAbsent(year, key -> new StatisticsDto(0, 0.0))
                            .addTemperature(temperature);

                }

                if (isFileChanged(url, fileSizeBefore)) {
                    savedTask.setMessage("File has changed during reading.");
                    savedTask.setChanged(true);
                }

                createStatistics(savedTask, map);

                taskService.updateTaskStatus(savedTask, TaskStatus.FINISHED);
                log.info("Finished task with id {}.", savedTask.getId());
            }
        } catch (Exception e) {
            savedTask.setMessage(e.getMessage());
            log.error(e.getMessage());
            taskService.updateTaskStatus(savedTask, TaskStatus.FAILED);
        }
    }

    private void createStatistics(final Task task, final Map<Integer, StatisticsDto> map) throws JsonProcessingException {
        statisticsRepository.save(Statistics.builder()
                .lastUpdate(LocalDateTime.now())
                .task(task)
                .result(objectMapper.writeValueAsString(map))
                .build());
    }

    private boolean isFileChanged(final URL url, final long fileSizeBefore) {
        long fileSizeAfter = getFileSize(url);
        return fileChangeDetector.detect(fileSizeBefore, fileSizeAfter);
    }

    private long getFileSize(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            return connection.getContentLengthLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
