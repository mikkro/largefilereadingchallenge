package com.kyotu.largefilereadingchallenge.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.repository.entity.Statistics;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsMapper {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private final ObjectMapper objectMapper;

    public StatisticsMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LocalDateTime mapDate(final String dateString) {
        try {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Error parsing the date and time string: " + e.getMessage());
        }
    }

    public List<StatisticsResponse> mapTemperature(final Map<Integer, StatisticsDto> map) {
        return map.entrySet()
                .stream()
                .map(entry -> new StatisticsResponse(
                        entry.getKey(),
                        calculateAverageTemperature(entry.getValue().getOccurrence(), entry.getValue().getTemperature()))
                )
                .toList();
    }


    private BigDecimal calculateAverageTemperature(final int occurrence, final double temperature) {
        return new BigDecimal(temperature / occurrence).setScale(1, RoundingMode.DOWN);
    }

    public Map<Integer, StatisticsDto> mapStatistics(Statistics statistics) {
        try {
            return objectMapper.readValue(statistics.getResult(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }

    }
}
