package ru.otus.mybooks.exception;

public class BookServiceBookNotFoundException extends MyBookAppException {
    private static final long serialVersionUID = 6320814069529160418L;

    public BookServiceBookNotFoundException(long bookNum) {
        super(String.format("The book with ID %d was not found in the database.", bookNum));
    }
}
