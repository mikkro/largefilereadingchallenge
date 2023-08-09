package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.repository.TaskRepository;
import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private FileConfiguration fileConfiguration;
    private Task task;

    @BeforeEach
    public void setup() {
        fileConfiguration = new FileConfiguration();
        task = Task.builder()
                .fileConfiguration(fileConfiguration)
                .city("SampleCity")
                .createDate(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .taskStatus(TaskStatus.PENDING)
                .fileSize(1024L)
                .build();
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(fileConfiguration, "SampleCity", 1024L);

        assertEquals(TaskStatus.PENDING, createdTask.getTaskStatus());
        assertEquals(1024L, createdTask.getFileSize());
    }

    @Test
    public void testFindFirstByFileConfigurationIdAndCity() {
        when(taskRepository.findFirstByFileConfigurationIdAndCityOrderByCreateDateDesc(anyLong(), anyString())).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.findFirstByFileConfigurationIdAndCity(1L, "SampleCity");

        assertTrue(foundTask.isPresent());
        assertEquals("SampleCity", foundTask.get().getCity());
    }
}
