package com.opsw.backend.config;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 런타임에 yml 상수를 참조하기 위한 전역 Bean
 */
@Component
public class AppConstants {

    private static AppProps props;

    public AppConstants(AppProps props) {
        AppConstants.props = props;
    }

    public static List<String> getQuestionTypes() {
        return props.getQuestionTypes();
    }

    public static List<String> getDifficultyLevels() {
        return props.getDifficultyLevels();
    }

    public static List<String> getTopcitCategories() {
        return props.getTopcitCategories();
    }
}
