package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.domain.Party;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.exceptions.QuestionsGettingException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final PartyService partyService;
    private final QuestionService questionService;
    private final InteractionService interactionService;
    private final AskingService askingService;

    private final QuizConfig quizConfig;
    private final MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);
    @Override
    public void startQuiz(Party party) {
        try {
            showPreamble(party);
            List<Question> questions = questionService.getQuestions();
            int allAnswerCount = questions.size();
            int rightAnswerCount = askQuestions(questions);
            showResult(party, rightAnswerCount, allAnswerCount);
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
            interactionService.say(messageSource.getMessage("quiz.cannot.run",
                    null, quizConfig.getQuizLocale()));
        }
    }

    private int askQuestions(List<Question> questions) {
        int rightAnswerCount = 0;
        for (Question question : questions) {
            if (askingService.askQuestion(question)) {
                rightAnswerCount++;
            }
        }
        return rightAnswerCount;
    }

    private void showResult(Party party, int rightAnswerCount, int allAnswerCount) {
        interactionService.say("");
        interactionService.say(messageSource.getMessage("participant.quiz.result",
                new String[]{party.getName(), Integer.toString(rightAnswerCount), Integer.toString(allAnswerCount)},
                quizConfig.getQuizLocale()));
        interactionService.say("");
    }

    private void showPreamble(Party party) {
        interactionService.say("");
        interactionService.say(messageSource.getMessage("questions.for.participant",
                new String[]{party.getName()}, quizConfig.getQuizLocale()));
        interactionService.say("");
    }

}
