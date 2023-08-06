package com.kyotu.largefilereadingchallenge.mapper;

import com.kyotu.largefilereadingchallenge.controller.dto.StatisticsResponse;
import com.kyotu.largefilereadingchallenge.service.dto.StatisticsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsMapper {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

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
}
