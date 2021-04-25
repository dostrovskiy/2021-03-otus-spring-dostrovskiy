package ru.otus.mybooks.dao;

import ru.otus.mybooks.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Genre insert(Genre genre);

    Optional<Genre> find(Genre genre);

    Optional<Genre> getById(long id);

    List<Genre> getAll();
}
