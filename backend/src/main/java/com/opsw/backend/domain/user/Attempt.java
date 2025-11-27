package com.opsw.backend.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "attempt")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // attempt.id

    @Column(name = "user_id", nullable = false)
    private Long userId;  // attempt.user_id

    @Column(name = "question_id", nullable = false)
    private Long questionId;  // attempt.question_id  (FK X)

    @Column(name = "submitted_answer", nullable = false, length = 255)
    private String submittedAnswer;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Column(name = "gained_xp", nullable = false)
    private int gainedXp;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;  // DB에서 자동 생성 (CURRENT_TIMESTAMP)
}
