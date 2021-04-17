package ru.otus.quiz.parsers;

import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionParserImpl implements QuestionParser {
    /**
     * @param input Вопросы, варианты ответов, правильный ответ должны быть каждый в отдельной строке.
     * <ul>
     * <li>Вопрос, должен быть завершен знаком вопроса ?
     * <li>Варианты ответов, завершаются точкой .
     * <li>Правильный ответ имеет знак восклицания ! в конце
     * </ul>
     * @return список вопросов {@link ru.otus.quiz.Question}
     * <p>Если отсутствует вопрос или правильный ответ, вопрос пропускается.
     */
    @Override
    public List<Question> parse(List<String> list) {
        List<Question> questions = new ArrayList<>();
        String questionText = "";
        String rightAnswer = "";
        List<String> answers = new ArrayList<>();
        for (String text : list) {
            if (text.endsWith("?")) {
                questionText = text;
                rightAnswer = "";
                answers.clear();
            }
            if (text.endsWith(".")) {
                answers.add(text);
            }
            if (text.endsWith("!")) {
                rightAnswer = text.substring(0, text.length() - 1);
            }
            if (!questionText.isBlank() && !rightAnswer.isBlank()) {
                Question question = new Question(questionText, rightAnswer, new ArrayList<>(answers));
                questions.add(question);
                questionText = "";
            }
        }
        return questions;
    }
}
