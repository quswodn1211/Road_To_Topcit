package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 문제 테이블 (자유/추천 공용)
 * 상수값(qtype, difficulty)은 yml에서 관리
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "question",
        indexes = @Index(name = "idx_question_subject", columnList = "subject_id"))
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문제 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 문제 본문 내용

    @Column(columnDefinition = "json")
    private String options; // 객관식 보기 (JSON)

    @Column(nullable = false, length = 255)
    private String answer; // 정답

    @Column(length = 20, nullable = false)
    private String qtype; // 문제 유형 (예: MCQ, SHORT) → yml에서 관리

    @Column(length = 20, nullable = false)
    private String difficulty; // 난이도 (예: Easy, Hard...) → yml에서 관리

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Subject subject; // 소속 과목

    @Column(columnDefinition = "json")
    private String tags; // 주제 태그

    @Column(columnDefinition = "TEXT")
    private String aiPrompt; // AI 생성 프롬프트

    @Column(columnDefinition = "TEXT")
    private String aiOutput; // AI 생성 결과
}
