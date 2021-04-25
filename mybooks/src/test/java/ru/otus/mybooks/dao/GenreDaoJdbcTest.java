package ru.otus.mybooks.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.mybooks.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@JdbcTest
@Import(GenreDaoJdbc.class)
@DisplayName("Класс GenreDaoJdbc должен")
class GenreDaoJdbcTest {
    @Autowired
    private GenreDao dao;

    @Test
    @DisplayName("добавлять жанр в базу данных")
    void shouldInsertGenre() {
        Genre expGenre = new Genre("Комедия");
        Genre actGenre = dao.insert(expGenre);
        assertThat(actGenre.getId()).isGreaterThan(0);
        assertThat(actGenre.getName()).isEqualTo(expGenre.getName());
    }

    @Test
    @DisplayName("находить жанр в базе данных")
    void shouldFindGenre() {
        Genre expGenre = new Genre("Комедия");
        dao.insert(expGenre);
        Genre actGenre = dao.find(expGenre).orElse(Genre.EMPTY_GENRE);
        assertThat(actGenre.getId()).isGreaterThan(0);
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("получать все жанры из базы данных")
    void shouldGetAllGenres() {
        Genre expGenre = new Genre("Комедия");
        dao.insert(expGenre);
        Genre expGenre2 = new Genre("Драма");
        dao.insert(expGenre2);
        List<Genre> Genres = dao.getAll();
        assertThat(Genres.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("находить жанр в базе данных по идентификатору")
    void shouldGetGenreById() {
        Genre expGenre = new Genre("Комедия");
        Genre insGenre = dao.insert(expGenre);
        Genre actGenre = dao.getById(insGenre.getId()).orElse(Genre.EMPTY_GENRE);
        assertThat(actGenre.getName()).isEqualTo(expGenre.getName());
    }

}