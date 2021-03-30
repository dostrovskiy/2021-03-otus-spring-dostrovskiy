package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс Сервис участника")
@ExtendWith(MockitoExtension.class)
class PartyServiceImplTest {

    public static final String MIKE_NAME = "Mike";
    public static final int MIKE_AGE = 45;

    @Mock
    private InteractionService interactionService;

    private PartyService partyService;

    @BeforeEach
    void setUp() {
        partyService = new PartyServiceImpl(interactionService);
        when(interactionService.ask("What is your name?")).thenReturn(MIKE_NAME);
        when(interactionService.ask("How old are you?")).thenReturn(Integer.toString(MIKE_AGE));
    }

    @Test
    @DisplayName("создал участника")
    void shouldGetParty() {
        Party party = partyService.getParty();
        assertThat(party.getName()).isEqualTo(MIKE_NAME);
        assertThat(party.getAge()).isEqualTo(MIKE_AGE);
    }
}