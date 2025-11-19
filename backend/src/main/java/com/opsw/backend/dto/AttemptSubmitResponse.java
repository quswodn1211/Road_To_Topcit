package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptSubmitResponse {

    private Long attemptId;
    private boolean isCorrect;
    private String correctAnswer;
    private int gainedXp;
}
