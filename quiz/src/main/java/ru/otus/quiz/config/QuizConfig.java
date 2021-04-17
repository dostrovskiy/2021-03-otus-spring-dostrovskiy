package ru.otus.quiz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "quiz")
@Getter
@Setter
public class QuizConfig {
    private List<QuizLocaleConfig> quizLocaleConfigList;
    private Locale quizLocale;

    public void setQuizLocaleName(String quizLocaleName) {
        quizLocale = Locale.forLanguageTag(quizLocaleName);
    }
}
