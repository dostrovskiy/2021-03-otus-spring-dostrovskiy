package ru.otus.quiz.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.quiz.domain.Question;

import java.util.List;

@Repository
public class QuestionDaoImpl implements QuestionDao {
    private final String resourceName;
    private final QuestionParser parser;

    public QuestionDaoImpl(@Value("${questions.filename}")String resourceName, QuestionParser parser) {
        this.resourceName = resourceName;
        this.parser = parser;
    }

    @Override
    public List<Question> getQuestions() throws QuestionsGettingException {
        try {
            return parser.parse(getClass().getClassLoader().getResourceAsStream(resourceName));
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
    }
}
