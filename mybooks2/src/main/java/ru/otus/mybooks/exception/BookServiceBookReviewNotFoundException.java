package ru.otus.mybooks.exception;

public class BookServiceBookReviewNotFoundException extends MyBookAppException {
    private static final long serialVersionUID = -8305696911454085183L;

    public BookServiceBookReviewNotFoundException(long reviewId) {
        super(String.format("The review with ID %d was not found in the book.", reviewId));
    }
}
