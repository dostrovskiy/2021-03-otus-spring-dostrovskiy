package ru.otus.quiz.service;

import ru.otus.quiz.domain.Question;

public interface AskingService {
    boolean askQuestion(Question question);
}
