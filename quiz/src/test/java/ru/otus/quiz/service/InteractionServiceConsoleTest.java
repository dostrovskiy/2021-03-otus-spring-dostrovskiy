package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс Сервиса общения с помощью консоли")
class InteractionServiceConsoleTest {
    public static final String MIKE_NAME = "Mike";
    public static final String MIKE_GREETING = "What`s up, Mike?";
    private InteractionService interactionService;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        InputStream inputStream = new ByteArrayInputStream(MIKE_NAME.getBytes());
        interactionService = new InteractionServiceConsole(inputStream, printStream);
    }

    @Test
    @DisplayName("должен говорить")
    void shouldSay() {
        interactionService.say(MIKE_GREETING);
        assertThat(out).hasToString(MIKE_GREETING+"\r\n");
    }

    @Test
    @DisplayName("должен спрашивать")
    void shouldAsk() {
        assertThat(interactionService.ask("What`s your name?")).isEqualTo(MIKE_NAME);
    }
}