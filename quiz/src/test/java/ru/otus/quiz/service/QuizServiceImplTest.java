package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.quiz.dao.QuestionsGettingException;
import ru.otus.quiz.domain.Party;
import ru.otus.quiz.domain.Question;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс Сервис Викторины")
class QuizServiceImplTest {
    public static final String MIKE_NAME = "Mike";
    public static final int MIKE_AGE = 45;
    public static final String SMART_QUESTION = "Java is the best language in the world, isn't it?";
    public static final String DUMB_QUESTION = "Brainfuck is the best language in the world, isn't it?";
    public static final String RIGHT_ANSWER = "Definitely!!!";

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImplTest.class);

    private QuizService quizService;
    @Mock
    private PartyService partyService;
    @Mock
    private QuestionService questionService;
    @Mock
    private AskingService askingService;

    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        InputStream inputStream = new ByteArrayInputStream(MIKE_NAME.getBytes());
        InteractionService interactionService = new InteractionServiceConsole(inputStream, printStream);
        quizService = new QuizServiceImpl(partyService, questionService, interactionService, askingService);

        Party party = new Party(MIKE_NAME, MIKE_AGE);
        List<Question> questionList = new ArrayList<>();
        Question smartQuestion = new Question(SMART_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        Question dumbQuestion = new Question(DUMB_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        questionList.add(smartQuestion);
        questionList.add(dumbQuestion);

        doReturn(party).when(partyService).getParty();
        try {
            doReturn(questionList).when(questionService).getQuestions();
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
        }
        doReturn(true).when(askingService).askQuestion(smartQuestion);
        doReturn(false).when(askingService).askQuestion(dumbQuestion);
    }

    @Test
    @DisplayName("должен провести викторину")
    void shouldStartQuiz() {
        quizService.startQuiz();
        assertThat(out.toString()).endsWith("Participant " + MIKE_NAME
                + " answered " + 1
                + " of " + 2 + " questions correctly!\r\n");
    }
}