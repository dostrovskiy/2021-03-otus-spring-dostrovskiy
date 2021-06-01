package ru.otus.mybooks.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.mybooks.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами должен")
@DataJpaTest
class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("получать информацию о жанре по имени")
    void shouldFindByName() {
        var genres = repository.findByName("Пьеса");
        var expGenres = List.of(em.find(Genre.class, 1L));
        assertThat(genres).usingFieldByFieldElementComparator().isEqualTo(expGenres);
    }
}