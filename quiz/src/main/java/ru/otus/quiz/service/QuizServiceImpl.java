package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import ru.otus.quiz.dao.QuestionsGettingException;
import ru.otus.quiz.domain.Party;
import ru.otus.quiz.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final PartyService partyService;
    private final QuestionService questionService;
    private final InteractionService interactionService;
    private final AskingService askingService;

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
            for (int i = 0; i < questions.size(); i++) {
                if (askingService.askQuestion(questions.get(i))) {
                    rightAnswerCount++;
                }
            }
            interactionService.say("Participant " + party.getName()
                    + " answered " + rightAnswerCount
                    + " of " + questions.size() + " questions correctly!");
        } catch (QuestionsGettingException e) {
            interactionService.say("Sorry, the quiz cannot be run. " + e.getMessage());
        }
        interactionService.finish();
    }
}
