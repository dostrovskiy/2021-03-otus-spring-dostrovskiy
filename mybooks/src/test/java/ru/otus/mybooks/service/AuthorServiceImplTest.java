package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.dao.AuthorDao;
import ru.otus.mybooks.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс AuthorServiceImpl должен")
class AuthorServiceImplTest {

    private AuthorService service;
    @Mock
    private AuthorDao dao;

    @Test
    @DisplayName("добавлять автора")
    void shouldAddAuthor() {
        service = new AuthorServiceImpl(dao);
        Author author = new Author("Пушкин А.С.");
        Author expAuthor = new Author(2, "Пушкин А.С.");

        doReturn(Optional.empty()).when(dao).find(author);
        doReturn(expAuthor).when(dao).insert(author);

        Author actAuthor = service.addAuthor(author);

        assertThat(actAuthor).usingRecursiveComparison().isEqualTo(expAuthor);
    }

    @Test
    @DisplayName("получать всех авторов")
    void shouldGetAllAuthors() {
        service = new AuthorServiceImpl(dao);
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        List<Author> list = List.of(author1, author2);

        doReturn(list).when(dao).getAll();

        List<String> actList = service.getAllAuthors();

        assertThat(actList).containsAll(List.of(author1.toString(), author2.toString()));
    }
}