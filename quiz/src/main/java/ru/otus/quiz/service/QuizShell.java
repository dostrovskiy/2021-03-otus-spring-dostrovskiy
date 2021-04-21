package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.config.QuizLocaleConfig;
import ru.otus.quiz.domain.Party;

@ShellComponent
@RequiredArgsConstructor
public class QuizShell {
    private Party party;
    private final QuizService quizService;
    private final QuizConfig quizConfig;
    private final MessageSource messageSource;
    private final InteractionService interactionService;

    private Availability isPartyExists() {
        return party == null ?
                Availability.unavailable(messageSource.getMessage("party.unavailable",
                        null, quizConfig.getQuizLocale())) :
                Availability.available();
    }

    @ShellMethod(key = "start", value = "Start Quizzz!!!")
    @ShellMethodAvailability(value = "isPartyExists")
    public void startQuiz() {
        quizService.startQuiz(party);
    }


    @ShellMethod(key = {"locales", "lc"}, value = "Show available locales")
    public void getLocales() {
        for (QuizLocaleConfig quizLocaleConfig : quizConfig.getQuizLocaleConfigList()) {
            interactionService.say(String.format("%d - %s", quizLocaleConfig.getId(), quizLocaleConfig.getRequestText()));
        }
    }

    @ShellMethod(key = {"set-locale", "sl"}, value = "Specify number for Quiz locale, e.g.: sl 2")
    public void setLocale(@ShellOption int localeId) {
        quizConfig.getQuizLocaleConfigList().stream()
                .filter(l -> l.getId() == localeId)
                .findFirst()
                .ifPresent(c -> quizConfig.setQuizLocaleName(c.getName()));
        interactionService.say(messageSource.getMessage("quiz.welcome", null, quizConfig.getQuizLocale()));
    }

    @ShellMethod(key = {"introduce", "im"}, value = "Introduce yourself, tell us your name and age, e.g.: im Mike 23")
    public void setPartyByNameAndAge(@ShellOption String name, int age) {
        party = new Party(name, age);
        interactionService.say(messageSource.getMessage("quiz.greeting",
                new String[]{party.getName()},
                quizConfig.getQuizLocale()));
    }

}
