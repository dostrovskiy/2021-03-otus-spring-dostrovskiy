package ru.otus.mybooks.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.mybooks.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами должен")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("сохранять информацию о жанре")
    void shouldSave() {
        val genre = new Genre(0, "Роман");
        repository.save(genre);
        assertThat(genre.getId()).isPositive();

        val actualGenre = em.find(Genre.class, genre.getId());
        assertThat(actualGenre).isNotNull().matches(a -> !a.getName().equals(""));
    }

    @Test
    @DisplayName("получать информацию о всех жанрах")
    void shouldFindAll() {
        val genres = repository.findAll();
        assertThat(genres).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("получать информацию о жанре по имени")
    void shouldFindByName() {
        val genres = repository.findByName("Пьеса");
        val expGenres = List.of(em.find(Genre.class, 1L));
        assertThat(genres).usingFieldByFieldElementComparator().isEqualTo(expGenres);
    }
}