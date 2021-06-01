package ru.otus.mybooks.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.mybooks.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами должен")
@DataJpaTest
class AuthorRepositoryJpaTest {
    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("получать информацию об авторе по имени")
    void shouldFindByName() {
        var authors = repository.findByName("Островский А.Н.");
        var expAuthors = List.of(em.find(Author.class, 1L));
        assertThat(authors).usingFieldByFieldElementComparator().isEqualTo(expAuthors);
    }
}