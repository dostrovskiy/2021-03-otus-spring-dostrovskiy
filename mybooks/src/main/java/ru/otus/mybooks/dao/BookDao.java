package ru.otus.mybooks.dao;

import ru.otus.mybooks.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> find(Book book);

    Optional<Book> getById(long id);

    List<Book> getAll();

    Book insert(Book book);

    void update(Book book);

    void deleteById(long id);
}
