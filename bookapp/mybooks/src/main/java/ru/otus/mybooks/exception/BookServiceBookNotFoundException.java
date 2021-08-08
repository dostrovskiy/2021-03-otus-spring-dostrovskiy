package ru.otus.mybooks.exception;

public class BookServiceBookNotFoundException extends MyBookAppException {

    public BookServiceBookNotFoundException(long bookId) {
        super(String.format("The book with ID %d was not found in the database.", bookId));
    }
}
