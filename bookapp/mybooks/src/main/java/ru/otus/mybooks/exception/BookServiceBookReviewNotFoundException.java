package ru.otus.mybooks.exception;

public class BookServiceBookReviewNotFoundException extends MyBookAppException {

    public BookServiceBookReviewNotFoundException(long reviewId) {
        super(String.format("The review with ID %d was not found in the book.", reviewId));
    }
}
