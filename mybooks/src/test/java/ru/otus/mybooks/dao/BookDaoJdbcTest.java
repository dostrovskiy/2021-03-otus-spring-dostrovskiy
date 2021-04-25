package ru.otus.mybooks.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
@DisplayName("Класс BookDaoJdbc должен")
class BookDaoJdbcTest {

    @Autowired
    private BookDao dao;

    @Test
    @DisplayName("добавлять книгу в базу данных")
    void shouldInsertBook() {
        Book expBook = new Book("Денискины рассказы", Author.EMPTY_AUTHOR, Genre.EMPTY_GENRE);
        Book actBook = dao.insert(expBook);
        assertThat(actBook.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("изменять книгу в базе данных")
    void shouldUpdateBook() {
        Book fndBook = dao.getById(2).orElse(Book.EMPTY_BOOK);

        assertThat(fndBook.getId()).isGreaterThan(0);
        assertThat(fndBook.getAuthor()).usingRecursiveComparison().isEqualTo(Author.EMPTY_AUTHOR);
        assertThat(fndBook.getGenre()).usingRecursiveComparison().isEqualTo(Genre.EMPTY_GENRE);

        Author author = new Author("Толстой Л.Н.");
        Genre genre = new Genre("роман-эпопея");
        Book updBook = new Book(fndBook.getId(), fndBook.getTitle(), author, genre);

        Book actBook = dao.update(updBook);

        assertThat(actBook.getId()).isEqualTo(fndBook.getId());
        assertThat(actBook.getAuthor()).usingRecursiveComparison().isEqualTo(updBook.getAuthor());
        assertThat(actBook.getGenre()).usingRecursiveComparison().isEqualTo(updBook.getGenre());
    }

    @Test
    @DisplayName("удалять книгу из базы данных")
    void shouldDeleteBookById() {
        Book fndBook = dao.getById(1).orElse(Book.EMPTY_BOOK);
        assertThat(fndBook.getId()).isGreaterThan(0);

        dao.deleteById(fndBook.getId());

        Book actBook = dao.getById(1).orElse(Book.EMPTY_BOOK);
        assertThat(actBook).usingRecursiveComparison().isEqualTo(Book.EMPTY_BOOK);
    }

    @Test
    @DisplayName("находить книгу в базе данных")
    void shouldFindBook() {
        Author author = new Author(1, "Островский А.Н.");
        Genre genre = new Genre(1, "Пьеса");
        Book fndBook = new Book("На всякого мудреца довольно простоты", author, genre);
        Book expBook = new Book(1, fndBook.getTitle(), fndBook.getAuthor(), fndBook.getGenre());

        Book actBook = dao.find(fndBook).orElse(Book.EMPTY_BOOK);

        assertThat(actBook).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("получать все книги из базы данных")
    void shouldGetAllBooks() {
        Author author = new Author(1, "Островский А.Н.");
        Genre genre = new Genre(1, "Пьеса");
        Book book1 = new Book(1, "На всякого мудреца довольно простоты", author, genre);
        Book book2 = new Book(2, "Война и мир", Author.EMPTY_AUTHOR, Genre.EMPTY_GENRE);

        List<Book> list = dao.getAll();

        assertThat(list.size()).isEqualTo(2);
        assertThat(list).containsExactly(book1, book2);
    }
}