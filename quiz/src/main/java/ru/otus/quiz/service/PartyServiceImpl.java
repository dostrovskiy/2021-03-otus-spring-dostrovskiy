package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Party;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {
    private final InteractionService interactionService;

    private static final Logger logger = LoggerFactory.getLogger(PartyServiceImpl.class);

    @Override
    public Party getParty() {
        String name = interactionService.ask("What is your name?");
        int age = 0;
        try {
            age = Integer.parseInt(interactionService.ask("How old are you?"));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return new Party(name, age);
    }
}
