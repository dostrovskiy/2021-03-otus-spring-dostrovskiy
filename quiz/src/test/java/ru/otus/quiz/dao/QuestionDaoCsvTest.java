package ru.otus.quiz.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.parsers.CsvParser;
import ru.otus.quiz.parsers.QuestionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Класс Dao вопросов")
@ExtendWith(MockitoExtension.class)
class QuestionDaoCsvTest {
    public static final String QUESTION = "What`s up?";
    public static final String RIGHT_ANSWER = "Great!";

    private static final Logger logger = LoggerFactory.getLogger(QuestionDaoCsvTest.class);

    private QuestionDao dao;
    @Mock
    private QuestionParser questionParser;
    @Mock
    private CsvParser csvParser;
    @Mock
    private QuizConfig quizConfig;

    @Test
    @DisplayName("должен получить список вопросов")
    void shouldGetQuestions() {
        dao = new QuestionDaoCsv(questionParser, csvParser, quizConfig);

        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(QUESTION, RIGHT_ANSWER, new ArrayList<>()));

        List<String> list = new ArrayList<>();
        list.add(QUESTION);
        list.add(RIGHT_ANSWER);

        doReturn(questionList).when(questionParser).parse(any());
        doReturn(list).when(csvParser).parse(any());
        doReturn(new Locale("en", "US")).when(quizConfig).getQuizLocale();

        assertAll(
                () -> assertThat(dao.getQuestions()).isNotNull(),
                () -> assertThat(dao.getQuestions()).hasSize(1),
                () -> assertThat(dao.getQuestions().get(0).getText()).isEqualTo(QUESTION),
                () -> assertThat(dao.getQuestions().get(0).getRightAnswer()).isEqualTo(RIGHT_ANSWER));
    }
}