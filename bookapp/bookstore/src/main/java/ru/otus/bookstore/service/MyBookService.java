package ru.otus.bookstore.service;

import ru.otus.bookstore.dto.BookDto;

import java.util.List;

public interface MyBookService {
    List<BookDto> getBooks();

    String getToken();
}
