package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    Author addAuthor(Author author);

    List<AuthorDto> getAllAuthors();
}
