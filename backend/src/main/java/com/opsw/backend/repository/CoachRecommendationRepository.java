package com.opsw.backend.repository;

import com.opsw.backend.domain.CoachRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRecommendationRepository extends JpaRepository<CoachRecommendation, Long> {

    // 사용자별 추천 로그 최신순 페이지 조회
    Page<CoachRecommendation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
