package com.kyotu.largefilereadingchallenge.service;

import com.kyotu.largefilereadingchallenge.exception.FileConfigurationNotFoundException;
import com.kyotu.largefilereadingchallenge.repository.FileConfigurationRepository;
import com.kyotu.largefilereadingchallenge.repository.entity.FileConfiguration;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileConfigurationService {

    private final FileConfigurationRepository fileConfigurationRepository;

    public FileConfigurationService(final FileConfigurationRepository fileConfigurationRepository) {
        this.fileConfigurationRepository = fileConfigurationRepository;
    }

    public void setFilePath(final String path) {
        validateConfigurationFileExistency(path);
        final FileConfiguration fileConfiguration = new FileConfiguration();
        fileConfiguration.setPath(path);
        fileConfigurationRepository.save(fileConfiguration);
    }

    private void validateConfigurationFileExistency(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileConfigurationNotFoundException("File not found.");
        }
    }

    public FileConfiguration getFileConfig() {
        return fileConfigurationRepository.findFirstByOrderByIdDesc().orElseThrow(() -> new FileConfigurationNotFoundException("File configuration not found."));
    }
}
