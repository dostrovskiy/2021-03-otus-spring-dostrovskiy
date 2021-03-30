package ru.otus.quiz;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.quiz.service.QuizService;

public class Main {
    public static void main(String[] args) {
        final var context = new ClassPathXmlApplicationContext("spring-context.xml");
        final QuizService quizService = context.getBean(QuizService.class);
        quizService.startQuiz();
    }
}
