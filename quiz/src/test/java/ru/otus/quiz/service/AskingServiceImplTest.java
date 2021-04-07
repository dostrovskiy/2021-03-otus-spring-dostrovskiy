package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Question;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс Сервиса задающего вопросы")
class AskingServiceImplTest {
    public static final String SMART_QUESTION = "Java is the best language in the world, isn't it?";
    public static final String DUMB_QUESTION = "Brainfuck is the best language in the world, isn't it?";
    public static final String RIGHT_ANSWER = "Definitely!!!";
    public static final String WRONG_ANSWER = "Oh, no!!!";

    private AskingService askingService;
    @Mock
    private InteractionService interactionService;

    @BeforeEach
    void setUp() {
        askingService = new AskingServiceImpl(interactionService);
    }

    @Test
    @DisplayName("должен задать умный вопрос")
    void shouldAskQuestion() {
        when(interactionService.ask("Your answer:")).thenReturn(RIGHT_ANSWER);
        Question smartQuestion = new Question(SMART_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        assertThat(askingService.askQuestion(smartQuestion)).isTrue();
    }

    @Test
    @DisplayName("должен задать глупый вопрос")
    void shouldAskMoreQuestion() {
        when(interactionService.ask("Your answer:")).thenReturn(WRONG_ANSWER);
        Question dumbQuestion = new Question(DUMB_QUESTION, RIGHT_ANSWER, new ArrayList<>());
        assertThat(askingService.askQuestion(dumbQuestion)).isFalse();
    }

}