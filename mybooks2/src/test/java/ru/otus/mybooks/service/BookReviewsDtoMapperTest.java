package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Конвертер отзывов книг должен")
class BookReviewsDtoMapperTest {
    @Autowired
    private BookReviewsDtoMapper bookReviewsMapper;

    @Configuration
    static class TestConfig {
        @Bean
        public BookDtoMapper getBookMapper() {
            return new BookDtoMapperImpl();
        }

        @Bean
        public BookReviewsDtoMapper getBookReviewsMapper() {
            var bookReviewsMapper = new BookReviewsDtoMapperImpl();
            bookReviewsMapper.setBookDtoMapper(getBookMapper());
            return bookReviewsMapper;
        }
    }

    @Test
    @DisplayName("конвертировать отзывы и данные книги")
    void shouldGetBookReviews() {
        var review = new Review(1, "Очень интересно!");
        var review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        var book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        var bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of());
        var expReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));

        var actReviewsDto = bookReviewsMapper.getBookReviewsDto(book);

        assertThat(actReviewsDto).usingRecursiveComparison().isEqualTo(expReviewsDto);
    }
}