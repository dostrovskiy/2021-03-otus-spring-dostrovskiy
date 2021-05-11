package ru.otus.mybooks.service;

import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.dto.BookDto;

public interface BookDtoConverter {
    BookDto getBookDto(Book book);
}
