package ru.otus.mybooks.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BookReviewsDto {
    private final String bookInfo;
    private final List<Review> reviews;

    public BookReviewsDto(Book book) {
        this.bookInfo = new BookDto(book).toString();
        this.reviews = new ArrayList<>(book.getReviews());
    }
}
