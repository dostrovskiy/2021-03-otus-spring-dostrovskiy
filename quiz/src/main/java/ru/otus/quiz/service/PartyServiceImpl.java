package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Party;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {
    private final InteractionService interactionService;

    @Override
    public Party getParty() {
        String name = interactionService.ask("What is your name?");
        int age = Integer.parseInt(interactionService.ask("How old are you?"));
        return new Party(name, age);
    }
}
