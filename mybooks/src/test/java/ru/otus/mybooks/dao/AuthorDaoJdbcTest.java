package ru.otus.mybooks.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.mybooks.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@JdbcTest
@Import(AuthorDaoJdbc.class)
@DisplayName("Класс AuthorDaoJdbc должен")
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDao dao;

    @Test
    @DisplayName("добавлять автора в базу данных")
    void shouldInsertAuthor() {
        Author expAuthor = new Author("Пушкин А.С.");
        Author actAuthor = dao.insert(expAuthor);
        assertThat(actAuthor.getId()).isGreaterThan(0);
        assertThat(actAuthor.getName()).isEqualTo(expAuthor.getName());
    }

    @Test
    @DisplayName("находить автора в базе данных")
    void shouldFindAuthor() {
        Author expAuthor = new Author("Пушкин А.С.");
        dao.insert(expAuthor);
        Author actAuthor = dao.find(expAuthor).orElse(Author.EMPTY_AUTHOR);
        assertThat(actAuthor.getId()).isGreaterThan(0);
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("получать всех авторов из базы данных")
    void shouldGetAllAuthors() {
        Author expAuthor = new Author(2, "Пушкин А.С.");
        dao.insert(expAuthor);
        Author expAuthor2 = new Author(3, "Лермонтов М.Ю.");
        dao.insert(expAuthor2);
        List<Author> authors = dao.getAll();
        assertThat(authors.size()).isEqualTo(3);
        assertThat(authors).containsAll(List.of(expAuthor, expAuthor2));
    }

    @Test
    @DisplayName("находить автора в базе данных по идентификатору")
    void shouldGetAuthorById() {
        Author expAuthor = new Author("Пушкин А.С.");
        Author insAuthor = dao.insert(expAuthor);
        Author actAuthor = dao.getById(insAuthor.getId()).orElse(Author.EMPTY_AUTHOR);
        assertThat(actAuthor.getName()).isEqualTo(expAuthor.getName());
    }
}