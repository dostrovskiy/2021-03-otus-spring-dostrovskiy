package ru.otus.mybooks.exception;

public class MyBookAppException extends RuntimeException{
    private static final long serialVersionUID = 2596369759187274606L;
    public MyBookAppException(String message) {
        super(message);
    }

}
