package com.kyotu.largefilereadingchallenge.controller.dto;

import java.math.BigDecimal;

public record StatisticsResponse(Integer year, BigDecimal averageTemperature){}
