package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Конвертер отзывов книг должен")
class BookReviewsDtoConverterImplTest {
    private BookReviewsDtoConverter converter;
    @Mock
    private BookDtoConverter bookDtoConverter;


    @Test
    @DisplayName("конвертировать отзывы и данные книги")
    void shouldGetBookReviews() {
        converter = new BookReviewsDtoConverterImpl(bookDtoConverter);
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        BookDto bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of());
        BookReviewsDto expReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));

        doReturn(bookDto).when(bookDtoConverter).getBookDto(book);

        BookReviewsDto actReviewsDto = converter.getBookReviews(book);

        assertThat(actReviewsDto).usingRecursiveComparison().isEqualTo(expReviewsDto);
    }
}