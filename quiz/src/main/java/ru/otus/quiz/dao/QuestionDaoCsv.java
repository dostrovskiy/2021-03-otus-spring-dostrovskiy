package ru.otus.quiz.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.exceptions.QuestionsGettingException;
import ru.otus.quiz.parsers.CsvParser;
import ru.otus.quiz.parsers.QuestionParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class QuestionDaoCsv implements QuestionDao {
    private final String resourceName;
    private final QuestionParser questionParser;
    private final CsvParser csvParser;

    public QuestionDaoCsv(@Value("${questions.filename}") String resourceName, QuestionParser questionParser, CsvParser csvParser) {
        this.resourceName = resourceName;
        this.questionParser = questionParser;
        this.csvParser = csvParser;
    }

    @Override
    public List<Question> getQuestions() {
        InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName);
        assert input != null;
        try (Reader reader = new InputStreamReader(input)) {
            return questionParser.parse(csvParser.parse(reader));
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
    }
}
