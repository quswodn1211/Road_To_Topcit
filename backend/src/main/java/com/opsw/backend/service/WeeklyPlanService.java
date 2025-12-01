package com.opsw.backend.service;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.domain.WeeklyPlan;
import com.opsw.backend.dto.WeeklyPlanSaveRequest;
import com.opsw.backend.repository.AttemptRepository;
import com.opsw.backend.repository.SubjectRepository;
import com.opsw.backend.repository.WeeklyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class WeeklyPlanService {

    private final WeeklyPlanRepository weeklyPlanRepository;
    private final SubjectRepository subjectRepository;
    private final AttemptRepository attemptRepository;

    private final Random random = new Random();

    /** ================================
     *   주간 계획 조회 (+ 자동 생성)
     *   ================================ */
    public Map<String, Object> getWeeklyPlan(Long userId, LocalDate weekStart) {

        Optional<WeeklyPlan> existing = weeklyPlanRepository.findByUserIdAndWeekStart(userId, weekStart);
        if (existing.isPresent()) {
            return existing.get().getPlan();
        }

        Map<String, Object> autoPlan = generateAutoPlan(userId);

        WeeklyPlan entity = WeeklyPlan.builder()
                .userId(userId)
                .weekStart(weekStart)
                .plan(autoPlan)
                .build();

        weeklyPlanRepository.save(entity);
        return autoPlan;
    }


    /** ================================
     *   주간 계획 저장/수정 기능
     *   ================================ */
    public void saveWeeklyPlan(Long userId, WeeklyPlanSaveRequest request) {

        LocalDate weekStart = request.getWeekStart();
        Map<String, Object> plan = request.getPlan();

        Optional<WeeklyPlan> existing = weeklyPlanRepository.findByUserIdAndWeekStart(userId, weekStart);

        WeeklyPlan entity;
        if (existing.isPresent()) {
            // 수정
            entity = existing.get();
            entity.updatePlan(plan);
        } else {
            // 새로 저장
            entity = WeeklyPlan.builder()
                    .userId(userId)
                    .weekStart(weekStart)
                    .plan(plan)
                    .build();
        }

        weeklyPlanRepository.save(entity);
    }


    /** ================================
     *   자동 계획 생성
     *   ================================ */
    private Map<String, Object> generateAutoPlan(Long userId) {

        List<Subject> subjects = subjectRepository.findAll();

        List<Object[]> xpResult = attemptRepository.getXpBySubject(userId);
        Map<Long, Integer> subjectXpMap = new HashMap<>();

        for (Object[] row : xpResult) {
            if (row[0] == null) continue;

            Long subjectId = ((Number) row[0]).longValue();
            Number xpNumber = (Number) row[1];
            int xpSum = xpNumber != null ? xpNumber.intValue() : 0;

            subjectXpMap.put(subjectId, xpSum);
        }

        subjects.forEach(s -> subjectXpMap.putIfAbsent(s.getId(), 0));

        int maxXp = subjectXpMap.values().stream().max(Integer::compareTo).orElse(0);

        Map<Long, Double> weights = new HashMap<>();
        for (Subject subject : subjects) {
            long subjectId = subject.getId();
            int xp = subjectXpMap.get(subjectId);

            double randomFactor = 10 + random.nextInt(11);
            double w = (maxXp - xp) + randomFactor;

            weights.put(subjectId, w);
        }

        double weightSum = weights.values().stream().mapToDouble(Double::doubleValue).sum();

        int dailyMinutes = 60;
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        Map<String, Object> weeklyPlan = new LinkedHashMap<>();

        for (String day : days) {

            List<Map<String, Object>> dailyList = new ArrayList<>();

            int allocated = 0;
            Map<Long, Integer> temp = new LinkedHashMap<>();

            for (Subject subject : subjects) {
                long subjectId = subject.getId();
                double ratio = weights.get(subjectId) / weightSum;

                int mins = (int) Math.round(dailyMinutes * ratio);
                if (mins < 1) mins = 1;

                temp.put(subjectId, mins);
                allocated += mins;
            }

            int diff = dailyMinutes - allocated;
            if (diff != 0) {

                long weakest = subjects.stream()
                        .min(Comparator.comparing(s -> subjectXpMap.get(s.getId())))
                        .get().getId();

                temp.put(weakest, temp.get(weakest) + diff);
            }

            for (Map.Entry<Long, Integer> e : temp.entrySet()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("subjectId", e.getKey());
                entry.put("mins", e.getValue());
                dailyList.add(entry);
            }

            weeklyPlan.put(day, dailyList);
        }

        return weeklyPlan;
    }
}
