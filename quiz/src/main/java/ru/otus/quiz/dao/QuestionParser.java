package ru.otus.quiz.dao;

import ru.otus.quiz.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface QuestionParser {
    List<Question> parse(InputStream input) throws IOException;
}
