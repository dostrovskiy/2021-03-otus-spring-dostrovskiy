package ru.otus.mybooks.exception;

public class BookServiceRemoveBookException extends MyBookAppException {
    private static final long serialVersionUID = -4689176991544730063L;

    public BookServiceRemoveBookException(String message) {
        super(message);
    }
}
