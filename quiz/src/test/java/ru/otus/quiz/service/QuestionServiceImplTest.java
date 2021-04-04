package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.dao.QuestionsGettingException;
import ru.otus.quiz.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс Сервис вопросов")
class QuestionServiceImplTest {
    public static final String QUESTION = "Got it?";
    public static final String RIGHT_ANSWER = "Yeah!";

    private QuestionService questionService;
    @Mock
    private QuestionDao dao;

    @BeforeEach
    void setUp() {
        questionService = new QuestionServiceImpl(dao);
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(QUESTION, RIGHT_ANSWER, new ArrayList<>()));
        try {
            when(dao.getQuestions()).thenReturn(questionList);
        } catch (QuestionsGettingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("должен получить список вопросов")
    void shouldGetQuestions() {
        try {
            assertThat(questionService.getQuestions()).isNotNull();
        } catch (QuestionsGettingException e) {
            e.printStackTrace();
        }
    }
}