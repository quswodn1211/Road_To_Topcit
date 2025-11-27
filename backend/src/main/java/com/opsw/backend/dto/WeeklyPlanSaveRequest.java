package com.opsw.backend.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
public class WeeklyPlanSaveRequest {
    private LocalDate weekStart;
    private Map<String, Object> plan;
}
