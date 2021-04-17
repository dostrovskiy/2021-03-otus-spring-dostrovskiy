package ru.otus.quiz.parsers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Класс Парсер вопросов")
@ExtendWith(MockitoExtension.class)
class QuestionParserImplTest {

    @Test
    @DisplayName("должен парсить вопросы")
    void shouldCorrectlyParse() {
        QuestionParser parser = new QuestionParserImpl();

        List<String> list = new ArrayList<>();
        list.add("Some cool question?");
        list.add("Right answer!");
        list.add("Another terrific question?");
        list.add("1. answer variant.");
        list.add("2. answer variant.");
        list.add("Right answer!");

        List<Question> questions = parser.parse(list);

        assertAll(
                () -> assertThat(questions).isNotNull(),
                () -> assertThat(questions).hasSize(2),
                () -> assertThat(questions.get(1).getAnswers().size()).isEqualTo(2)
        );
    }
}
