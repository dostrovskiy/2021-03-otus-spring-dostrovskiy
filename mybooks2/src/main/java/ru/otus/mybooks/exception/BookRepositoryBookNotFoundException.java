package ru.otus.mybooks.exception;

public class BookRepositoryBookNotFoundException extends MyBookAppException {

    public BookRepositoryBookNotFoundException(long id) {
        super(String.format("The book with ID %d was not found in the database.", id));
    }
}
