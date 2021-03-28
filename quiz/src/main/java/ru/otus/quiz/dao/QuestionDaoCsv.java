package ru.otus.quiz.dao;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.otus.quiz.domain.Question;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {
    private final List<Question> questions = new ArrayList<>();
    private final String fileName;

    @Override
    public List<Question> getQuestions() throws QuestionsGettingException {
        try {
            InputStream resource = getClass().getClassLoader().getResourceAsStream(fileName);
            assert resource != null;
            Reader reader = new InputStreamReader(resource);
            String[] HEADERS = {"question", "answer"};
            CSVParser csvParser = CSVFormat.DEFAULT.withHeader(HEADERS).parse(reader);
            for (CSVRecord record : csvParser) {
                Question question = new Question(record.get(HEADERS[0]), record.get(HEADERS[1]));
                questions.add(question);
            }
            reader.close();
        } catch (Exception e) {
            throw new QuestionsGettingException("Error getting the list of questions.", e);
        }
        return questions;
    }
}
