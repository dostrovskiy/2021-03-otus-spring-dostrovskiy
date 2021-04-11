package ru.otus.quiz.parsers;

import ru.otus.quiz.domain.Question;

import java.util.List;

public interface QuestionParser {
    List<Question> parse(List<String> list);
}
