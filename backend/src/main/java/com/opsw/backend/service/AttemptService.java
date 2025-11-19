package com.opsw.backend.service;

import com.opsw.backend.domain.Question;
import com.opsw.backend.domain.user.Attempt;
import com.opsw.backend.dto.AttemptSubmitRequest;
import com.opsw.backend.dto.AttemptSubmitResponse;
import com.opsw.backend.repository.AttemptRepository;
import com.opsw.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    public AttemptSubmitResponse submitAttempt(AttemptSubmitRequest request) {

        // 1. 문제 조회
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // 2. 정답 체크
        boolean isCorrect = question.getAnswer().equals(request.getSubmittedAnswer());

        // 3. XP 계산
        int gainedXp = isCorrect ? 10 : 0;

        // 4. 저장
        Attempt attempt = Attempt.builder()
                .userId(request.getUserId())
                .questionId(request.getQuestionId())
                .submittedAnswer(request.getSubmittedAnswer())
                .isCorrect(isCorrect)
                .gainedXp(gainedXp)
                .build();

        attemptRepository.save(attempt);

        // 5. 응답 변환
        return new AttemptSubmitResponse(
                attempt.getId(),
                isCorrect,
                question.getAnswer(),
                gainedXp
        );
    }
}
