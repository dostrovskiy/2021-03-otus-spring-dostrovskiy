package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Genre;

import java.util.List;

public interface GenreService {
    Genre addGenre(Genre genre);

    List<String> getAllGenres();
}
