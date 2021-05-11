package ru.otus.mybooks.repositories;

import ru.otus.mybooks.domain.Genre;

import java.util.List;

public interface GenreRepository {
    Genre save(Genre genre);

    List<Genre> findAll();

    List<Genre> findByName(String name);
}
