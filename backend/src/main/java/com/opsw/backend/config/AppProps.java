package com.opsw.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * application.yml에서 상수들을 주입받는 클래스
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.constants")
public class AppProps {
    private List<String> questionTypes;       // 문제 유형
    private List<String> difficultyLevels;    // 난이도
    private List<String> topcitCategories;    // TOPCIT 카테고리
}
