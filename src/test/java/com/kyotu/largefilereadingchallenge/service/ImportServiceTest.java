package com.kyotu.largefilereadingchallenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyotu.largefilereadingchallenge.mapper.StatisticsMapper;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImportServiceTest {

    @Mock
    private StatisticsMapper statisticsMapper;

    @Mock
    private TaskService taskService;

    @Mock
    private FileConfigurationService fileConfigurationService;


    private ImportService importService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void init() {
        importService = new ImportService(this.statisticsMapper, this.taskService, this.objectMapper, this.fileConfigurationService);
    }

    @Test
    public void testStartImport_Success() throws Exception {
        File testFile = File.createTempFile("testFile", ".txt");

        String expectedResult = objectMapper.writeValueAsString(Map.of(2023, new StatisticsDto(2, 50.0)));

        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.println("""
                    SampleCity;2022-01-01;20.0
                    SampleCity;2022-01-01;30.0
                    """);
        }

        final Task task = Task.builder()
                .id(1L)
                .city("SampleCity")
                .build();
        when(taskService.updateTaskStatus(eq(task), eq(TaskStatus.IN_PROGRESS))).thenReturn(task);

        FileConfiguration fileConfiguration = new FileConfiguration();
        fileConfiguration.setPath(testFile.getAbsolutePath());
        when(fileConfigurationService.getFileConfig()).thenReturn(fileConfiguration);

        when(statisticsMapper.mapDate(anyString())).thenReturn(LocalDateTime.now());


        importService.startImport(task);
        ArgumentCaptor<Task> savedTaskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskService, times(1)).updateTaskStatus(savedTaskCaptor.capture(), eq(TaskStatus.FINISHED));
        String capturedSavedTask = savedTaskCaptor.getValue().getStatistics().getResult();

        assertThat(capturedSavedTask).isEqualTo(expectedResult);

        testFile.delete();
    }

}