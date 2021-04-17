package ru.otus.quiz.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.dao.QuestionDaoCsv;
import ru.otus.quiz.domain.Question;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(properties = {"command.line.runner.enabled=false"})
@DisplayName("Класс Сервис Викторины")
class QuizServiceImplIntegrationTest {
    public static final String LOCALE_ID = "1";
    public static final String MIKE_NAME = "Mike";
    public static final String MIKE_AGE = "45";
    public static final String SMART_QUESTION = "Java is the best language in the world, isn't it?";
    public static final String DUMB_QUESTION = "Brainfuck is the best language in the world, isn't it?";
    public static final String RIGHT_ANSWER = "Definitely!!!";
    public static final String WRONG_ANSWER = "Oh, no!!!";

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImplIntegrationTest.class);

    @MockBean
    private QuizConfig quizConfig;
    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private QuizService quizService;

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @TestConfiguration
    static class QuizServiceImplIntegrationTestConfig {
        @Bean
        public InteractionService interactionService() {
            PrintStream printStream = new PrintStream(out);
            String testInput = LOCALE_ID + "\r\n" +
                    MIKE_NAME + "\r\n" +
                    MIKE_AGE + "\r\n" +
                    RIGHT_ANSWER + "\r\n" +
                    WRONG_ANSWER + "\r\n";
            InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
            return new InteractionServiceConsole(inputStream, printStream);
        }
    }

    @Test
    @DisplayName("должен провести викторину")
    void shouldStartQuiz() {
        List<Question> questionList = new ArrayList<>();
        Question smartQuestion = new Question(SMART_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        Question dumbQuestion = new Question(DUMB_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        questionList.add(smartQuestion);
        questionList.add(dumbQuestion);

        doReturn(new Locale("en", "US")).when(quizConfig).getQuizLocale();
        doReturn(questionList).when(questionDao).getQuestions();

        quizService.startQuiz();

        assertThat(out.toString())
                .endsWith(String.format("Participant %s answered %d of %d questions correctly!\r\n", MIKE_NAME, 1, 2));
    }
}