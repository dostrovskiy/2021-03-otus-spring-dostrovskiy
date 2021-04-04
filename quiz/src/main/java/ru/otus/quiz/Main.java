package ru.otus.quiz;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.otus.quiz.service.QuizService;

@PropertySource("classpath:application.properties")
@ComponentScan
public class Main {
    public static void main(String[] args) {
        final var context = new AnnotationConfigApplicationContext(Main.class);
        final QuizService quizService = context.getBean(QuizService.class);
        quizService.startQuiz();
    }
}
