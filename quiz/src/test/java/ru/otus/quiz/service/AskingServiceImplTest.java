package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Question;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Класс Сервиса задающего вопросы")
@ExtendWith(MockitoExtension.class)
class AskingServiceImplTest {
    public static final String SMART_QUESTION = "Java is the best language in the world, isn't it?";
    public static final String DUMB_QUESTION = "Brainfuck is the best language in the world, isn't it?";
    public static final String RIGHT_ANSWER = "Definitely!!!";
    public static final String WRONG_ANSWER = "Oh, no!!!";

    @Mock
    private InteractionService interactionService;
    @Mock
    private QuizConfig quizConfig;
    @Mock
    private MessageSource messageSource;

    private AskingService askingService;

    @Test
    @DisplayName("должен задать умный вопрос")
    void shouldAskQuestion() {
        askingService = new AskingServiceImpl(interactionService, messageSource, quizConfig);
        when(interactionService.ask(any())).thenReturn(RIGHT_ANSWER);
        Question smartQuestion = new Question(SMART_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        assertThat(askingService.askQuestion(smartQuestion)).isTrue();
    }

    @Test
    @DisplayName("должен задать глупый вопрос")
    void shouldAskMoreQuestion() {
        askingService = new AskingServiceImpl(interactionService, messageSource, quizConfig);
        when(interactionService.ask(any())).thenReturn(WRONG_ANSWER);
        Question dumbQuestion = new Question(DUMB_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        assertThat(askingService.askQuestion(dumbQuestion)).isFalse();
    }

}