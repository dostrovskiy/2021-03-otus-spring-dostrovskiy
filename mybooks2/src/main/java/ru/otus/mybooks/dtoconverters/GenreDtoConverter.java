package ru.otus.mybooks.dtoconverters;

import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;

public interface GenreDtoConverter {
    GenreDto getGenreDto(Genre genre);
}
