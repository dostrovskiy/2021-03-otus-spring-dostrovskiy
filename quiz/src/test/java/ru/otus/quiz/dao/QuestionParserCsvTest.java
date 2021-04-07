package ru.otus.quiz.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.quiz.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс Парсер вопросов в формате csv")
class QuestionParserCsvTest {
    private final QuestionParserCsv parser = new QuestionParserCsv();
    private List<Question> questionList;

    @BeforeEach
    void setUp() {
        try {
            questionList = parser.parse(getClass().getClassLoader().getResourceAsStream("questions.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("должен парсить csv")
    void shouldParseResourceCorrectly() {
        assertThat(questionList).isNotNull();
        assertThat(questionList).hasSize(2);
        assertThat(questionList.get(1).getAnswers().size()).isEqualTo(4);
    }
}