package com.opsw.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 허용
                .allowedOrigins("*") // 모든 도메인 허용
                .allowedMethods("*") // GET, POST, PUT, DELETE 등 모든 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // Credential 필요 없으면 false
    }
}
