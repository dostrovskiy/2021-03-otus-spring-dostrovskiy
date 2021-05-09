package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    @Transactional
    @Override
    public Author addAuthor(Author author) {
        return repository.findByName(author.getName()).stream().findFirst().orElseGet(() -> repository.save(author));
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllAuthors() {
        return repository.findAll().stream().map(Author::toString).collect(Collectors.toList());
    }
}
