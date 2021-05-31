package ru.otus.mybooks.dtoconverters;

import org.springframework.stereotype.Service;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;

@Service
public class GenreDtoConverterImpl implements GenreDtoConverter {
    @Override
    public GenreDto getGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
