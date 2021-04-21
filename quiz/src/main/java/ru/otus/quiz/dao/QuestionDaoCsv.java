package ru.otus.quiz.dao;

import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {
    private final QuestionParser questionParser;
    private final CsvParser csvParser;
    private final QuizConfig quizConfig;

    @Override
    public List<Question> getQuestions() {
        try (Reader reader = new InputStreamReader(getQuestionsFileAsStream())) {
            return questionParser.parse(csvParser.parse(reader));
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
    }

    private InputStream getQuestionsFileAsStream() {
        String localizedQuestionsFileName = getLocalizedFileName(quizConfig.getCsvFileName(),
                quizConfig.getQuizLocale().toString());
        InputStream input = getClass().getClassLoader().getResourceAsStream(localizedQuestionsFileName);
        return input != null ? input : getClass().getClassLoader().getResourceAsStream(quizConfig.getCsvFileName());
    }

    private String getLocalizedFileName(String fileName, String localeTag) {
        return getPureFileName(fileName) + "_" + localeTag + getFileNameExt(fileName);
    }

    private String getPureFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private String getFileNameExt(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(dotIndex);
    }
}
