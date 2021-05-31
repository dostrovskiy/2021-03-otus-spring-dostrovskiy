package ru.otus.mybooks.dtoconverters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.dto.BookReviewsDto;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BookReviewsDtoConverterImpl implements BookReviewsDtoConverter {
    private final BookDtoConverter bookDtoConverter;

    @Override
    public BookReviewsDto getBookReviews(Book book) {
        return BookReviewsDto.builder()
                .bookInfo(bookDtoConverter.getBookDto(book).toString())
                .reviews(new ArrayList<>(book.getReviews()))
                .build();
    }
}
