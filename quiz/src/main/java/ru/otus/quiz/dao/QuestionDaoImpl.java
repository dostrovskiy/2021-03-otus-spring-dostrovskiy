package ru.otus.quiz.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.quiz.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {
    private final String resourceName;
    private final QuestionParser parser;

    @Override
    public List<Question> getQuestions() throws QuestionsGettingException {
        try {
            return parser.parse(getClass().getClassLoader().getResourceAsStream(resourceName));
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
    }
}
