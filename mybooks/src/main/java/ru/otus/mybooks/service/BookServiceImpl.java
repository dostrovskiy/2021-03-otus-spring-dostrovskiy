package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.dao.BookDao;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.dto.BookDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao dao;

    @Override
    public List<BookDto> getAllBooks() {
        return dao.getAll().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto addBook(Book book) {
        return new BookDto(dao.find(book).orElseGet(() -> dao.insert(book)));
    }

    @Override
    public BookDto saveBook(Book book) {
        return new BookDto(dao.find(book).orElseGet(() -> dao.update(book)));
    }

    @Override
    public void removeBook(long bookNum) {
        dao.deleteById(bookNum);
    }
}
