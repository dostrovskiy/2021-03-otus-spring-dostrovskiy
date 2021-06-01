package ru.otus.mybooks.dto;

import org.mapstruct.Mapper;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookDtoMapper {
    BookDto getBookDto(Book book);

    default List<String> mapAuthor(List<Author> authors) {
        return authors.stream().map(Author::getName).collect(Collectors.toList());
    }

    default List<String> mapGenre(List<Genre> genres) {
        return genres.stream().map(Genre::getName).collect(Collectors.toList());
    }
}
