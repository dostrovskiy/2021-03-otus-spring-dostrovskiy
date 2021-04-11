package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    @Override
    public List<Question> getQuestions() {
        return dao.getQuestions();
    }
}
