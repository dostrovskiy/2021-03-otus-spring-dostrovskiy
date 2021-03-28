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

    @Override
    public void startQuiz() {
        int rightAnswerCount = 0;
        try {
            interactionService.say("Welcome to our quiz!");
            List<Question> questions = questionService.getQuestions();
            Party party = partyService.getParty();
            interactionService.say("Questions for the participant " + party.getName() + "!");
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                String answer = interactionService.ask(question.getQuestion());
                if (answer.equalsIgnoreCase(question.getAnswer())) {
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
