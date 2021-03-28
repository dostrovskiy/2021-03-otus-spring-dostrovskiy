package ru.otus.quiz.dao;

import ru.otus.quiz.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getQuestions() throws QuestionsGettingException;
}
