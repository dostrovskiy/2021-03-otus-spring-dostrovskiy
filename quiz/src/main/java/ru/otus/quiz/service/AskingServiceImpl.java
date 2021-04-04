package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Question;

@Service
@RequiredArgsConstructor
public class AskingServiceImpl implements AskingService {
    private final InteractionService interactionService;

    @Override
    public boolean askQuestion(Question question) {
        interactionService.say(question.getText());
        for (int i = 0; i < question.getAnswers().size(); i++) {
            interactionService.say(question.getAnswers().get(i));
        }
        String answer = interactionService.ask("Your answer:");
        interactionService.say("");
        return answer.equalsIgnoreCase(question.getRightAnswer());
    }
}
