package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookDtoMapper;
import ru.otus.mybooks.dto.BookDtoMapperImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер данных книги должен")
class BookDtoMapperTest {
    private BookDtoMapper bookMapper;

    @Test
    @DisplayName("конвертировать данные книги")
    void shouldGetBookDto() {
        bookMapper = new BookDtoMapperImpl();
        var author = new Author(1, "Пушкин А.С.");
        var genre = new Genre(1, "Поэма");
        var book = new Book(1, "Руслан и Людмила", List.of(author), List.of(genre), List.of());
        var expDto = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        var actBookDto = bookMapper.getBookDto(book);

        assertThat(actBookDto).usingRecursiveComparison().isEqualTo(expDto);
    }
}