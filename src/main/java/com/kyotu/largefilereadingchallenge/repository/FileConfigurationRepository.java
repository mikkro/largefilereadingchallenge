package com.kyotu.largefilereadingchallenge.repository;

import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileConfigurationRepository extends JpaRepository<FileConfiguration, Long> {
    Optional<FileConfiguration> findFirstByOrderByIdDesc();
}
