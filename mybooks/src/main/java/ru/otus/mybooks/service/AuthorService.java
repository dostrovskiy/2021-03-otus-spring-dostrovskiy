package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Author;

import java.util.List;

public interface AuthorService {
    Author addAuthor(Author author);

    List<String> getAllAuthors();
}
