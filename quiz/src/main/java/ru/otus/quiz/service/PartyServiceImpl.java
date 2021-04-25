package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Party;

@ConditionalOnBean(name = "quizRunner")
@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {
    private final InteractionService interactionService;
    private final MessageSource messageSource;
    private final QuizConfig quizConfig;

    private static final Logger logger = LoggerFactory.getLogger(PartyServiceImpl.class);

    @Override
    public Party getParty() {
        String name = interactionService.ask(messageSource.getMessage("whats.your.name",
                null, quizConfig.getQuizLocale()));
        int age = 0;
        try {
            age = Integer.parseInt(interactionService.ask(messageSource.getMessage("whats.your.age",
                    null, quizConfig.getQuizLocale())));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return new Party(name, age);
    }
}
