package ru.otus.mybooks.repositories;

import ru.otus.mybooks.domain.Author;

import java.util.List;

public interface AuthorRepository {
    Author save(Author author);

    List<Author> findAll();

    List<Author> findByName(String name);
}
