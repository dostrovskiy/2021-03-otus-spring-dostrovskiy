package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Question;

@Service
@RequiredArgsConstructor
public class AskingServiceImpl implements AskingService {
    private final InteractionService interactionService;
    private final MessageSource messageSource;
    private final QuizConfig quizConfig;

    @Override
    public boolean askQuestion(Question question) {
        interactionService.say(question.getText());
        for (int i = 0; i < question.getAnswers().size(); i++) {
            interactionService.say(question.getAnswers().get(i));
        }
        String answer = interactionService.ask(messageSource.getMessage("your.answer",
                null, quizConfig.getQuizLocale()));
        interactionService.say("");
        return answer.equalsIgnoreCase(question.getRightAnswer());
    }
}
