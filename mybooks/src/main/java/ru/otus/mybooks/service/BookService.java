package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto addBook(Book book);

    BookDto saveBook(Book book);

    void removeBook(long bookNum);
}
