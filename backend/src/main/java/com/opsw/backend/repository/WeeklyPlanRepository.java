package com.opsw.backend.repository;

import com.opsw.backend.domain.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {

    // 특정 주(week_start 기준)의 계획 1건 조회
    Optional<WeeklyPlan> findByUserIdAndWeekStart(Long userId, LocalDate weekStart);


    // 기간 내 주간 계획 목록
    List<WeeklyPlan> findByUserIdAndWeekStartBetweenOrderByWeekStartAsc(Long userId, LocalDate start, LocalDate end);
}
