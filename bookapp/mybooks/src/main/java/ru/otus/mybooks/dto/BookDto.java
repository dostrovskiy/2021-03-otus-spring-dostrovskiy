package ru.otus.mybooks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class BookDto {
    private final long id;
    private final String title;
    private final List<String> authors;
    private final List<String> genres;
    private final String isbn;

    @Override
    public String toString() {
        return id + ". " + title +
                (authors.isEmpty() ? "" : "; " + String.join(", ", authors)) +
                (genres.isEmpty() ? "" : "; " + String.join(", ", genres));
    }
}
