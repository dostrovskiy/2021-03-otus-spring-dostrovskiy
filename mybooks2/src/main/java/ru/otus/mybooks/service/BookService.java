package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    Book addBook(BookDto bookDto);

    Book editBook(BookDto bookDto);

    void removeBook(long bookNum);

    void addBookAuthor(long bookNum, Author author);

    void addBookGenre(long bookNum, Genre genre);

    void addBookReview(long bookNum, Review review);

    void removeBookReview(long bookNum, long reviewId);

    void editBookReview(long bookNum, Review review);

    BookReviewsDto getBookReviewsByNum(long bookNum);

    List<BookReviewsDto> getAllBookReviews();
}
