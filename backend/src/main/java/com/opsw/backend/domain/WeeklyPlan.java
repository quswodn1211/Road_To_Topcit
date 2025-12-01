package com.opsw.backend.domain;

import com.opsw.backend.config.JsonToMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "weekly_plan",
        indexes = @Index(name = "idx_weekly_plan_user_week", columnList = "user_id,week_start"))
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Convert(converter = JsonToMapConverter.class)
    @Column(name = "plan", columnDefinition = "json", nullable = false)
    private Map<String, Object> plan;

    public void updatePlan(Map<String, Object> plan) {
        this.plan = plan;
    }

}
