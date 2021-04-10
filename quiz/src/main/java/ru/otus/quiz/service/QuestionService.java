package ru.otus.quiz.service;

import ru.otus.quiz.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions();
}
