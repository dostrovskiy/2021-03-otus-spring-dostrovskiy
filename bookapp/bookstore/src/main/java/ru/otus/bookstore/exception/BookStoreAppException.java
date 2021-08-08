package ru.otus.bookstore.exception;

public class BookStoreAppException extends RuntimeException{

    public BookStoreAppException(String message, Throwable e) {
        super(message, e);
    }

}
