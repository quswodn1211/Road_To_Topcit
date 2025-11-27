package com.opsw.backend.controller;

import com.opsw.backend.dto.WeeklyPlanSaveRequest;
import com.opsw.backend.service.WeeklyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weekly-plan")
public class WeeklyPlanController {

    private final WeeklyPlanService weeklyPlanService;

    /** ================================
     *   GET: 주간 계획 조회
     * ================================ */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getWeeklyPlan(
            @RequestParam Long userId,
            @RequestParam LocalDate weekStart
    ) {
        return ResponseEntity.ok(weeklyPlanService.getWeeklyPlan(userId, weekStart));
    }

    /** ================================
     *   POST: 주간 계획 저장/수정
     * ================================ */
    @PostMapping
    public ResponseEntity<?> saveOrUpdateWeeklyPlan(
            @RequestParam Long userId,
            @RequestBody WeeklyPlanSaveRequest request
    ){
        weeklyPlanService.saveWeeklyPlan(userId, request);
        return ResponseEntity.ok(Map.of("message", "주간 학습 계획 저장/수정 완료"));
    }

}
