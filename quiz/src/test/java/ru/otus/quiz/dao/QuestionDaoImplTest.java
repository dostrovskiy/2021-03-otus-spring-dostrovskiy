package ru.otus.quiz.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@DisplayName("Класс Dao вопросов")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {
    public static final String QUESTION = "What`s up?";
    public static final String RIGHT_ANSWER = "Great!";

    private QuestionDao dao;
    @Mock
    private QuestionParser parser;

    @Test
    @DisplayName("должен вернуть список вопросов")
    void shouldGetQuestions() {
        dao = new QuestionDaoImpl("questions.csv", parser);
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(QUESTION, RIGHT_ANSWER, new ArrayList<>()));
        try {
            doReturn(questionList).when(parser).parse(any());
            assertThat(dao.getQuestions()).isNotNull();
            assertThat(dao.getQuestions()).hasSize(1);
            assertThat(dao.getQuestions().get(0).getText()).isEqualTo(QUESTION);
            assertThat(dao.getQuestions().get(0).getRightAnswer()).isEqualTo(RIGHT_ANSWER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("должен вернуть ошибку, что список не получен")
    void shouldThrowGetQuestionsException() {
        dao = new QuestionDaoImpl("wrong_resource_name.csv", parser);
        try {
            doThrow(new IOException()).when(parser).parse(any());
            List<Question> list = dao.getQuestions();
        } catch (Exception e) {
            assertThat(e).hasMessageStartingWith("Error getting the list of questions.");
        }
    }
}