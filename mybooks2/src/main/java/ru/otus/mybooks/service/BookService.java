package ru.otus.mybooks.service;

import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;
import ru.otus.mybooks.dto.ReviewDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto getBookById(long id);

    BookDto addBook(BookDto bookDto);

    BookDto editBook(BookDto bookDto);

    void removeBook(long bookId);

    BookReviewsDto addBookReview(long bookId, ReviewDto reviewDto);

    void removeBookReview(long bookId, long reviewId);

    BookReviewsDto getBookReviews(long bookId);

    List<BookReviewsDto> getAllBookReviews();
}
