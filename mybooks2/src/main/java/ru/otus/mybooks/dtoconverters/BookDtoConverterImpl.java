package ru.otus.mybooks.dtoconverters;

import org.springframework.stereotype.Service;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.BookDto;

import java.util.stream.Collectors;

@Service
public class BookDtoConverterImpl implements BookDtoConverter {
    @Override
    public BookDto getBookDto(Book book) {
            return BookDto.builder()
                    .authors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()))
                    .genres(book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()))
                    .title(book.getTitle())
                    .id(book.getId())
                    .build();
    }
}
