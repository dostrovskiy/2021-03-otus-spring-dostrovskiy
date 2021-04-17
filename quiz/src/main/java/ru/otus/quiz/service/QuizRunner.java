package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        prefix = "command.line.runner",
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@Component
@RequiredArgsConstructor
public class QuizRunner implements CommandLineRunner {
    private final QuizService quizService;

    @Override
    public void run(String... args) {
        quizService.startQuiz();
    }
}
