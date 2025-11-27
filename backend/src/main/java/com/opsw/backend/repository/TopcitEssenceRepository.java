package com.opsw.backend.repository;

import com.opsw.backend.domain.TopcitEssence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopcitEssenceRepository extends JpaRepository<TopcitEssence, Long> {

    // 과목 ID로 에센스 페이지 조회
    Page<TopcitEssence> findBySubject_Id(Long subjectId, Pageable pageable);

    // 카테고리 포함 검색(대소문자 무시)
    Page<TopcitEssence> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    // 본문 내용 키워드 검색(간단 LIKE)
    Page<TopcitEssence> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
}
