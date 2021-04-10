package ru.otus.quiz.exceptions;

public class QuestionsGettingException extends RuntimeException {
    private static final long serialVersionUID = -6583747357137013046L;

    public QuestionsGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
