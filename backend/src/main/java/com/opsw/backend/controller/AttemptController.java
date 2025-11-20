package com.opsw.backend.controller;

import com.opsw.backend.dto.AttemptSubmitRequest;
import com.opsw.backend.dto.AttemptSubmitResponse;
import com.opsw.backend.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping
    public ResponseEntity<AttemptSubmitResponse> submitAttempt(
            @RequestBody AttemptSubmitRequest request) {

        AttemptSubmitResponse response = attemptService.submitAttempt(request);
        return ResponseEntity.ok(response);
    }
}
