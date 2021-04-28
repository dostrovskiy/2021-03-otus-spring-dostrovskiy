package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.dao.GenreDao;
import ru.otus.mybooks.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс GenreServiceImpl должен")
class GenreServiceImplTest {
    private GenreService service;
    @Mock
    private GenreDao dao;

    @Test
    @DisplayName("добавлять автора")
    void shouldAddGenre() {
        service = new GenreServiceImpl(dao);
        Genre Genre = new Genre("Поэма");
        Genre expGenre = new Genre(2, "Поэма");

        doReturn(Optional.empty()).when(dao).find(Genre);
        doReturn(expGenre).when(dao).insert(Genre);

        Genre actGenre = service.addGenre(Genre);

        assertThat(actGenre).usingRecursiveComparison().isEqualTo(expGenre);
    }

    @Test
    @DisplayName("получать всех авторов")
    void shouldGetAllGenres() {
        service = new GenreServiceImpl(dao);
        Genre Genre1 = new Genre(1, "Пьеса");
        Genre Genre2 = new Genre(2, "Поэма");
        List<Genre> list = List.of(Genre1, Genre2);

        doReturn(list).when(dao).getAll();

        List<String> actList = service.getAllGenres();

        assertThat(actList).containsAll(List.of(Genre1.toString(), Genre2.toString()));
    }

}