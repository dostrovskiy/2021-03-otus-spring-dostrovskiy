package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.quiz.dao.QuestionsGettingException;
import ru.otus.quiz.domain.Party;
import ru.otus.quiz.domain.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final PartyService partyService;
    private final QuestionService questionService;
    private final InteractionService interactionService;
    private final AskingService askingService;

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Override
    public void startQuiz() {
        int rightAnswerCount = 0;
        try {
            interactionService.say("Welcome to our quiz!");
            interactionService.say("");
            Party party = partyService.getParty();
            interactionService.say("");
            interactionService.say("Questions for the participant " + party.getName() + "!");
            interactionService.say("");
            List<Question> questions = questionService.getQuestions();
            for (Question question : questions) {
                if (askingService.askQuestion(question)) {
                    rightAnswerCount++;
                }
            }
            interactionService.say("Participant " + party.getName()
                    + " answered " + rightAnswerCount
                    + " of " + questions.size() + " questions correctly!");
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
            interactionService.say("Sorry, the quiz cannot be run. " + e.getMessage());
        }
        interactionService.finish();
    }
}
