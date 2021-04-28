package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.dao.AuthorDao;
import ru.otus.mybooks.domain.Author;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao dao;

    @Override
    public Author addAuthor(Author author) {
        return dao.find(author).orElseGet(() -> dao.insert(author));
    }

    @Override
    public List<String> getAllAuthors() {
        return dao.getAll().stream().map(Author::toString).collect(Collectors.toList());
    }
}
