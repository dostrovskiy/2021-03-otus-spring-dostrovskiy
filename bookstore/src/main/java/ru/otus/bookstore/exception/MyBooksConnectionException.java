package ru.otus.bookstore.exception;

public class MyBooksConnectionException extends BookStoreAppException{

    public MyBooksConnectionException(String message, Throwable e) {
        super(message, e);
    }
}
