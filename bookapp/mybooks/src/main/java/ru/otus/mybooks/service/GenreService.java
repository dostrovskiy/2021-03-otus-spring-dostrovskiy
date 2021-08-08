package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;

import java.util.List;

public interface GenreService {
    Genre addGenre(Genre genre);

    List<GenreDto> getAllGenres();
}
