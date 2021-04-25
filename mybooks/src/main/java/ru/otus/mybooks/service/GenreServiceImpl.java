package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.dao.GenreDao;
import ru.otus.mybooks.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao dao;

    @Override
    public Genre addGenre(Genre genre) {
        return dao.find(genre).orElseGet(() -> dao.insert(genre));
    }

    @Override
    public List<String> getAllGenres() {
        return dao.getAll().stream().map(Genre::toString).collect(Collectors.toList());
    }
}
