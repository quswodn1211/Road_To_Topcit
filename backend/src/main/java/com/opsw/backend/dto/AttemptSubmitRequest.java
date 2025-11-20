package com.opsw.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttemptSubmitRequest {

    private Long userId;
    private Long questionId;
    private String submittedAnswer;
}
