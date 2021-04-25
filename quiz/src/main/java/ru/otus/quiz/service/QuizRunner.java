package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.config.QuizLocaleConfig;
import ru.otus.quiz.domain.Party;

@ConditionalOnProperty(
        prefix = "quiz",
        value = "console-enabled",
        havingValue = "true",
        matchIfMissing = false)
@Component
@RequiredArgsConstructor
public class QuizRunner implements CommandLineRunner {
    private final QuizService quizService;
    private final PartyService partyService;
    private final QuizConfig quizConfig;
    private final MessageSource messageSource;
    private final InteractionService interactionService;

    @Override
    public void run(String... args) {
        getQuizLocale();
        Party party = partyService.getParty();
        quizService.startQuiz(party);
    }

    private void getQuizLocale() {
        interactionService.say("");
        for (QuizLocaleConfig quizLocaleConfig : quizConfig.getQuizLocaleConfigList()) {
            interactionService.say(String.format("%d - %s", quizLocaleConfig.getId(), quizLocaleConfig.getRequestText()));
        }
        int localeId = Integer.parseInt(interactionService.ask(""));
        QuizLocaleConfig quizLocaleConfig = quizConfig.getQuizLocaleConfigList().stream()
                .filter(l -> l.getId() == localeId)
                .findFirst()
                .orElse(new QuizLocaleConfig());
        quizConfig.setQuizLocaleName(quizLocaleConfig.getName());
    }
}
