package ru.otus.quiz.exceptions;

public class CsvParsingException extends RuntimeException {
    private static final long serialVersionUID = -4328944546635835284L;

    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
