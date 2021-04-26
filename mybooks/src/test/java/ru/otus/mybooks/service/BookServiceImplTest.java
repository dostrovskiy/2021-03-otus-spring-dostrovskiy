package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.dao.BookDao;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.exception.BookServiceRemoveBookException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс BookServiceImpl должен")
class BookServiceImplTest {
    private BookService service;
    @Mock
    private BookDao dao;

    @Test
    @DisplayName("получать все книги")
    void shouldGetAllBooks() {
        service = new BookServiceImpl(dao);
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        Genre genre = new Genre(1, "Поэма");
        Book book1 = new Book(1, "Руслан и Людмила", author1, genre);
        Book book2 = new Book(2, "Мцыри", author2, genre);
        List<Book> list = List.of(book1, book2);
        BookDto dto1 = new BookDto(book1);
        BookDto dto2 = new BookDto(book2);

        doReturn(list).when(dao).getAll();

        List<BookDto> actList = service.getAllBooks();

        assertThat(actList.get(0)).usingRecursiveComparison().isEqualTo(dto1);
        assertThat(actList.get(1)).usingRecursiveComparison().isEqualTo(dto2);
    }

    @Test
    @DisplayName("добавлять новую книгу")
    void shouldAddBook() {
        service = new BookServiceImpl(dao);
        Author author = new Author(1, "Пушкин А.С.");
        Genre genre = new Genre(1, "Поэма");
        Book book = new Book(1, "Руслан и Людмила", author, genre);
        BookDto expDto = new BookDto(book);

        doReturn(Optional.empty()).when(dao).find(book);
        doReturn(book).when(dao).insert(book);

        BookDto actDto = service.addBook(book);

        assertThat(actDto).usingRecursiveComparison().isEqualTo(expDto);
    }

    @Test
    @DisplayName("сохранять отредактированную книгу")
    void shouldSaveBook() {
        service = new BookServiceImpl(dao);
        Author author = new Author(1, "Пушкин А.С.");
        Genre genre = new Genre(1, "Поэма");
        Book book = new Book(1, "Руслан и Людмила", author, genre);
        BookDto expDto = new BookDto(book);

        doReturn(Optional.empty()).when(dao).find(book);
        ArgumentCaptor<Book> valueCapture = ArgumentCaptor.forClass(Book.class);
        doNothing().when(dao).update(valueCapture.capture());

        service.saveBook(book);

        assertEquals(book, valueCapture.getValue());
    }

    @Test
    @DisplayName("удалять книгу")
    void shouldRemoveBook() {
        service = new BookServiceImpl(dao);
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        Genre genre = new Genre(1, "Поэма");
        Book book1 = new Book(1, "Руслан и Людмила", author1, genre);
        Book book2 = new Book(2, "Мцыри", author2, genre);
        List<Book> list = new ArrayList<>(List.of(book1, book2));

        doReturn(Optional.of(book1)).when(dao).getById(anyLong());
        doAnswer(i -> list.remove(0)).when(dao).deleteById(1);

        service.removeBook(1);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(book2);
    }
    @Test
    @DisplayName("выдать ошибку, если удаляемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfBookToRemoveNotFoundById() {
        service = new BookServiceImpl(dao);

        doReturn(Optional.empty()).when(dao).getById(anyLong());

        assertThatExceptionOfType(BookServiceRemoveBookException.class).isThrownBy(() -> service.removeBook(55));
    }
}