package com.kyotu.largefilereadingchallenge.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
class StatisticsMapperTest {

    private final StatisticsMapper statisticsMapper = new StatisticsMapper();



    @Test
    void shouldMapDate() {
        String stringDate = "2023-03-08 20:01:02.123";
        LocalDateTime result = statisticsMapper.mapDate(stringDate);

        assertThat(result.getYear()).isEqualTo(2023);
        assertThat(result.getMonth().getValue()).isEqualTo(3);
        assertThat(result.getDayOfMonth()).isEqualTo(8);
        assertThat(result.getHour()).isEqualTo(20);
        assertThat(result.getMinute()).isEqualTo(01);
        assertThat(result.getSecond()).isEqualTo(2);
    }

}