package com.kyotu.largefilereadingchallenge.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatisticsDto {
    private int occurrence;
    private double temperature;

    public void addTemperature(double temperature) {
        this.temperature += temperature;
        this.occurrence++;
    }
}
