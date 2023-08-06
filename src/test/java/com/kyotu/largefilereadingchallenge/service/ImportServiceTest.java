package com.kyotu.largefilereadingchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.controller.dto.TaskStatusResponse;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ImportServiceTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void shouldImportFailWithNotFoundMessage() {
        Task task = new Task();
        task.setId(1L);
        task.setUri("https://example.com/test.csv");

        importService.startImport(task);
        List<TaskStatusResponse> savedTasks = taskService.getTasksStatus();

        assertThat(savedTasks.size()).isEqualTo(1);
        assertThat(savedTasks.get(0).id()).isEqualTo(task.getId());
        assertThat(savedTasks.get(0).taskStatus()).isEqualTo(TaskStatus.FAILED);
        assertThat(savedTasks.get(0).message()).isEqualTo(String.format("%s", task.getUri()));

    }

    @Test
    void shouldImportFailWithWrongUrlMessage() {
        Task task = new Task();
        task.setId(1L);
        task.setUri("test");

        importService.startImport(task);
        List<TaskStatusResponse> savedTasks = taskService.getTasksStatus();

        assertThat(savedTasks.size()).isEqualTo(1);
        assertThat(savedTasks.get(0).id()).isEqualTo(task.getId());
        assertThat(savedTasks.get(0).taskStatus()).isEqualTo(TaskStatus.FAILED);
        assertThat(savedTasks.get(0).message()).isEqualTo(String.format("no protocol: %s", task.getUri()));

    }

    @Test
    void shouldImportSuccess() throws JsonProcessingException {
        Task task = new Task();
        task.setId(1L);
        task.setUri("https://mikolaj-kyotu.s3.eu-north-1.amazonaws.com/example_file.csv");

        List<StatisticsResponse> expectedResponse = List.of(
                new StatisticsResponse(2018, BigDecimal.valueOf(15.2)),
                new StatisticsResponse(2019, BigDecimal.valueOf(14.9)),
                new StatisticsResponse(2020, BigDecimal.valueOf(15.3)),
                new StatisticsResponse(2021, BigDecimal.valueOf(14.8)),
                new StatisticsResponse(2022, BigDecimal.valueOf(15.1)),
                new StatisticsResponse(2023, BigDecimal.valueOf(15.0)));


        importService.startImport(task);
        List<TaskStatusResponse> savedTasks = taskService.getTasksStatus();
        List<StatisticsResponse> statistics = statisticsService.getStatisticsForTask(task.getId());

        assertThat(savedTasks.size()).isEqualTo(1);
        assertThat(savedTasks.get(0).id()).isEqualTo(task.getId());
        assertThat(savedTasks.get(0).taskStatus()).isEqualTo(TaskStatus.FINISHED);
        assertThat(savedTasks.get(0).message()).isEqualTo(null);

        assertThat(statistics.size()).isEqualTo(expectedResponse.size());

        assertThat(statistics.get(0)).isEqualTo(expectedResponse.get(0));
        assertThat(statistics.get(1)).isEqualTo(expectedResponse.get(1));
        assertThat(statistics.get(2)).isEqualTo(expectedResponse.get(2));
        assertThat(statistics.get(3)).isEqualTo(expectedResponse.get(3));
        assertThat(statistics.get(4)).isEqualTo(expectedResponse.get(4));

    }

}