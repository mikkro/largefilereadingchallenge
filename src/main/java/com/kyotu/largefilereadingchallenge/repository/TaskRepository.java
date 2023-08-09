package com.kyotu.largefilereadingchallenge.repository;

import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findFirst100ByTaskStatusOrderByCreateDateAsc(TaskStatus taskStatus);

    List<Task> findAllByTaskStatus(TaskStatus taskStatus);

    Optional<Task> findFirstByFileConfigurationIdAndCityOrderByCreateDateDesc(Long configurationId, String city);

    List<Task> findAllByOrderByCreateDateAsc();
}
