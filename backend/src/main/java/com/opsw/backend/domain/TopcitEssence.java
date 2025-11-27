package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/** TOPCIT 핵심 요약 테이블 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "topcit_essence",
        indexes = {
                @Index(name = "idx_topcit_essence_category", columnList = "category"),
                @Index(name = "idx_topcit_essence_subject", columnList = "subject_id")
        })
public class TopcitEssence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 요약 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Subject subject; // 과목

    @Column(length = 100, nullable = false)
    private String category; // 카테고리 (yml에서 관리)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 요약 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시각
}
