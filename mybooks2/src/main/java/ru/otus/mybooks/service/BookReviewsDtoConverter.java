package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.dto.BookReviewsDto;

public interface BookReviewsDtoConverter {
    BookReviewsDto getBookReviews(Book book);
}
