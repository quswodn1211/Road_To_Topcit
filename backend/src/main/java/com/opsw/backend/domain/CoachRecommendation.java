package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/** AI 코치 추천 및 분석 로그 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coach_recommendation",
        indexes = @Index(name = "idx_coach_rec_user", columnList = "user_id"))
public class CoachRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 로그 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(columnDefinition = "json", nullable = false)
    private String payload; // 추천 내용(JSON)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시각
}
