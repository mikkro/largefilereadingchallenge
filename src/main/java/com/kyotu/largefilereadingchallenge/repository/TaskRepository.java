package com.kyotu.largefilereadingchallenge.repository;

import com.kyotu.largefilereadingchallenge.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findFirst100ByTaskStatusOrderByCreateDateAsc(TaskStatus taskStatus);

    List<Task> findAllByTaskStatus(TaskStatus taskStatus);

    List<Task> findAllByOrderByCreateDateAsc();
}
