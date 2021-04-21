package ru.otus.quiz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class QuizLocaleConfig {
    private int id;
    @Value("${quiz.quiz-locale-name}")
    private String name;
    private String requestText;
}
