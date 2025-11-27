package com.opsw.backend.repository;

import com.opsw.backend.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // 과목명으로 조회 (대소문자 무시)
    Optional<Subject> findByNameIgnoreCase(String name);

    // 과목명 중복 체크
    boolean existsByNameIgnoreCase(String name);
}
