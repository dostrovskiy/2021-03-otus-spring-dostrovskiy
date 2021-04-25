package ru.otus.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.exceptions.QuestionsGettingException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс Сервис вопросов")
class QuestionServiceImplTest {
    public static final String QUESTION = "Got it?";
    public static final String RIGHT_ANSWER = "Yeah!";

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImplTest.class);

    @Mock
    private QuestionDao dao;

    private QuestionService questionService;

    @Test
    @DisplayName("должен получить список вопросов")
    void shouldGetQuestions() {
        questionService = new QuestionServiceImpl(dao);
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(QUESTION, RIGHT_ANSWER, new ArrayList<>()));
        try {
            when(dao.getQuestions()).thenReturn(questionList);
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
        }

        try {
            assertThat(questionService.getQuestions()).isNotNull();
        } catch (QuestionsGettingException e) {
            logger.error(e.getMessage());
        }
    }
}