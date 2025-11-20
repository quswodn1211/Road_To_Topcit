package com.opsw.backend.repository;

import com.opsw.backend.domain.user.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
}
