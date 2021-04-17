package ru.otus.quiz.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.exceptions.QuestionsGettingException;
import ru.otus.quiz.parsers.CsvParser;
import ru.otus.quiz.parsers.QuestionParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {
    private final QuestionParser questionParser;
    private final CsvParser csvParser;
    private final QuizConfig quizConfig;

    @Override
    public List<Question> getQuestions() {
        InputStream input = getClass().getClassLoader().getResourceAsStream(getQuestionsFileName());
        assert input != null;
        try (Reader reader = new InputStreamReader(input)) {
            return questionParser.parse(csvParser.parse(reader));
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
    }

    private String getQuestionsFileName() {
        Locale questionsLocale = quizConfig.getQuizLocale();
        return ResourceBundle.getBundle("i18n/questions", questionsLocale).getString("filename");
    }
}
