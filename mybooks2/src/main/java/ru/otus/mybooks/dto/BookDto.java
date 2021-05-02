package ru.otus.mybooks.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class BookDto {
    private final long num;
    private final String title;
    private final List<String> authors;
    private final List<String> genres;

    public BookDto(Book book) {
        this.num = book.getId();
        this.title = book.getTitle();
        this.authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
        this.genres = book.getGenres().stream().map(Genre::getName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return num + ". " + title +
                (authors.isEmpty() ? "" : "; " + String.join(", ", authors)) +
                (genres.isEmpty() ? "" : "; " + String.join(", ", genres));
    }
}
