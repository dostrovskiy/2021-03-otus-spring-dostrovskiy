package ru.otus.mybooks.dto;

import org.mapstruct.Mapper;
import ru.otus.mybooks.domain.Genre;

@Mapper(componentModel = "spring")
public interface GenreDtoMapper {
    GenreDto getGenreDto(Genre genre);
}
