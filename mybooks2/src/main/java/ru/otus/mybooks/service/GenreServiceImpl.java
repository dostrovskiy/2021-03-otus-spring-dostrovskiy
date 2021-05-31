package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;
import ru.otus.mybooks.dtoconverters.GenreDtoConverter;
import ru.otus.mybooks.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;
    private final GenreDtoConverter dtoConverter;

    @Transactional
    @Override
    public Genre addGenre(Genre genre) {
        return repository.findByName(genre.getName()).stream().findFirst().orElseGet(() -> repository.save(genre));
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> getAllGenres() {
        return repository.findAll().stream().map(dtoConverter::getGenreDto).collect(Collectors.toList());
    }
}
