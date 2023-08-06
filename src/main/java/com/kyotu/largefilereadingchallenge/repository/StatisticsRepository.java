package com.kyotu.largefilereadingchallenge.repository;

import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Statistics findByTaskId(Long id);

    void deleteStatisticsByTaskIdIn(List<Long> taskIds);
}
