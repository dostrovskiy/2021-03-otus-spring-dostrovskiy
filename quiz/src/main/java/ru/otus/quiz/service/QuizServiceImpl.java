package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizConfig;
import ru.otus.quiz.config.QuizLocaleConfig;
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
    public void startQuiz() {
        int rightAnswerCount = 0;
        try {
            interactionService.say("====================================================");
            getQuizLocale();
            Party party = partyService.getParty();
            interactionService.say("");
            interactionService.say(messageSource.getMessage("questions.for.participant",
                    new String[]{party.getName()}, quizConfig.getQuizLocale()));
            interactionService.say("");
            List<Question> questions = questionService.getQuestions();
            for (Question question : questions) {
                if (askingService.askQuestion(question)) {
                    rightAnswerCount++;
                }
            }
            interactionService.say(messageSource.getMessage("participant.quiz.result",
                    new String[]{party.getName(), Integer.toString(rightAnswerCount), Integer.toString(questions.size())},
                    quizConfig.getQuizLocale()));
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
            interactionService.say(messageSource.getMessage("quiz.cannot.run",
                    null, quizConfig.getQuizLocale()));
        }
        interactionService.finish();
    }

    private void getQuizLocale() {
        for (QuizLocaleConfig quizLocaleConfig : quizConfig.getQuizLocaleConfigList()) {
            interactionService.say(messageSource.getMessage("quiz.choose.locale",
                    new String[]{quizLocaleConfig.getRequestText(), Integer.toString(quizLocaleConfig.getId())},
                    quizConfig.getQuizLocale()));
        }
        int localeId = Integer.parseInt(interactionService.ask(""));
        QuizLocaleConfig quizLocaleConfig = quizConfig.getQuizLocaleConfigList().stream()
                .filter(l -> l.getId() == localeId)
                .findFirst()
                .orElse(new QuizLocaleConfig());
        quizConfig.setQuizLocaleName(quizLocaleConfig.getName());
    }
}
