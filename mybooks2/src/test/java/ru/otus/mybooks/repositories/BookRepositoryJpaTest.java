package ru.otus.mybooks.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.exception.BookRepositoryBookNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Репозиторий на основе Jpa для работы с книгами должен")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("сохранять книгу")
    void shouldSave() {
        val author = new Author(0, "Островский А.Н.");
        val authors = Collections.singletonList(author);
        val genre = new Genre(0, "Пьеса");
        val genres = Collections.singletonList(genre);
        val review = new Review(0, "Прекрасная пьеса!!");
        val reviews = Collections.singletonList(review);


        val book = new Book(0, "На всякого мудреца довольно простоты", authors, genres, reviews);
        repository.save(book);
        assertThat(book.getId()).isPositive();

        val actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().matches(b -> !b.getTitle().equals(""))
                .matches(b -> b.getReviews() != null && b.getReviews().size() > 0 && b.getReviews().get(0).getId() > 0)
                .matches(b -> b.getAuthors() != null && b.getAuthors().size() > 0)
                .matches(b -> b.getGenres() != null && b.getGenres().size() > 0);
    }

    @Test
    @DisplayName("находить книгу по идентификатору")
    void shouldFindById() {
        val optionalActualBook = repository.findById(1L);
        val expectedBook = em.find(Book.class, 1L);
        assertThat(optionalActualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("находить все книги")
    void shouldFindAll() {
        val books = repository.findAll();
        val expBook = em.find(Book.class, 1L);
        val expBook2 = em.find(Book.class, 2L);
        val list = List.of(expBook, expBook2);
        assertThat(books).usingFieldByFieldElementComparator().isEqualTo(list);
    }

    @Test
    @DisplayName("удалять книгу по идентификатору")
    void shouldDeleteById() {
        val book = em.find(Book.class, 1L);
        assertThat(book).isNotNull();
        em.detach(book);

        repository.deleteById(1L);
        val deletedBook = em.find(Book.class, 1L);

        assertThat(deletedBook).isNull();
    }

    @Test
    @DisplayName("выдавать ошибку, если удаляемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfDeletingBookNotFoundById() {
        assertThatExceptionOfType(BookRepositoryBookNotFoundException.class).isThrownBy(() -> repository.deleteById(55L));
    }
}