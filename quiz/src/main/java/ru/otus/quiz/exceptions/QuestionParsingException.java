package ru.otus.quiz.exceptions;

public class QuestionParsingException extends RuntimeException {
    private static final long serialVersionUID = -5485716118118125270L;

    public QuestionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
