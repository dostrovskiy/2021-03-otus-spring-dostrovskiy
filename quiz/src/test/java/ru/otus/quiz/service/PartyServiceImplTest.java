package ru.otus.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Party;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("Класс Сервис участника")
@ExtendWith(MockitoExtension.class)
class PartyServiceImplTest {
    public static final String MIKE_NAME = "Mike";
    public static final int MIKE_AGE = 45;

    @Mock
    private InteractionService interactionService;
    @Mock
    private QuizConfig quizConfig;
    @Mock
    private MessageSource messageSource;

    private PartyService partyService;

    @Test
    @DisplayName("создал участника")
    void shouldGetParty() {
        partyService = new PartyServiceImpl(interactionService, messageSource, quizConfig);
        when(quizConfig.getQuizLocale()).thenReturn(new Locale("en", "US"));
        when(messageSource.getMessage(eq("whats.your.name"), any(), any())).thenReturn("What is your name?");
        when(messageSource.getMessage(eq("whats.your.age"), any(), any())).thenReturn("How old are you?");
        when(interactionService.ask("What is your name?")).thenReturn(MIKE_NAME);
        when(interactionService.ask("How old are you?")).thenReturn(Integer.toString(MIKE_AGE));

        Party party = partyService.getParty();

        assertThat(party.getName()).isEqualTo(MIKE_NAME);
        assertThat(party.getAge()).isEqualTo(MIKE_AGE);
    }
}