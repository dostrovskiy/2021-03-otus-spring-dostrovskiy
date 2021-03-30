package ru.otus.quiz.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.otus.quiz.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class QuestionParserCsv implements QuestionParser {
    @Override
    public List<Question> parse(InputStream input) throws IOException {
        List<Question> questions = new ArrayList<>();
        String[] columns = {"column"};
        String questionText = "", rightAnswer = "";
        List<String> answers = new ArrayList<>();
        try (Reader reader = new InputStreamReader(input)) {
            CSVParser parser = CSVFormat.DEFAULT.withHeader(columns).parse(reader);
            for (CSVRecord record : parser) {
                String text = record.get(columns[0]);
                if (text.endsWith("?")) {
                    questionText = text;
                    rightAnswer = "";
                    answers.clear();
                }
                if (text.endsWith(".")) {
                    answers.add(text);
                }
                if (text.endsWith("!")) {
                    rightAnswer = text.substring(0, text.length() - 1);
                }
                if (!questionText.isBlank() && !rightAnswer.isBlank()) {
                    Question question = new Question(questionText, rightAnswer, new ArrayList<>(answers));
                    questions.add(question);
                    questionText = "";
                }
            }
        }
        return questions;
    }

}
