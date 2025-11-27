package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class WeeklyPlanResponse {
    private Long userId;
    private LocalDate weekStart;
    private Map<String, Object> plan;
}

