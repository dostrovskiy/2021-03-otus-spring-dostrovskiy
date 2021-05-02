package ru.otus.mybooks.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.mybooks.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами должен")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {
    @Autowired
    private AuthorRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("сохранять информацию об авторе")
    void shouldSave() {
        val author = new Author(0, "Пушкин А.С.");
        repository.save(author);
        assertThat(author.getId()).isPositive();

        val actualAuthor = em.find(Author.class, author.getId());
        assertThat(actualAuthor).isNotNull().matches(a -> !a.getName().equals(""));
    }

    @Test
    @DisplayName("получать информацию о всех авторах")
    void shouldFindAll() {
        val authors = repository.findAll();
        assertThat(authors).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("получать информацию об авторе по имени")
    void shouldFindByName() {
        val authors = repository.findByName("Островский А.Н.");
        val expAuthors = List.of(em.find(Author.class, 1L));
        assertThat(authors).usingFieldByFieldElementComparator().isEqualTo(expAuthors);
    }
}