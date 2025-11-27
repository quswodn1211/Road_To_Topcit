package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AttemptResponse {
    private Long attemptId;
    private Long userId;
    private Long questionId;
    private String submittedAnswer;
    private boolean isCorrect;
    private int gainedXp;
    private String createdAt;
}
