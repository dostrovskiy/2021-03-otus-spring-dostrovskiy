package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс GenreServiceImpl должен")
class GenreServiceImplTest {
    private GenreService service;
    @Mock
    private GenreRepository repository;

    @Test
    @DisplayName("добавлять жанр")
    void shouldAddGenre() {
        service = new GenreServiceImpl(repository);
        Genre Genre = new Genre(0, "Поэма");
        Genre expGenre = new Genre(2, "Поэма");

        doReturn(List.of()).when(repository).findByName("Поэма");
        doReturn(expGenre).when(repository).save(Genre);

        Genre actGenre = service.addGenre(Genre);

        assertThat(actGenre).usingRecursiveComparison().isEqualTo(expGenre);
    }

    @Test
    @DisplayName("получать все жанры")
    void shouldGetAllGenres() {
        service = new GenreServiceImpl(repository);
        Genre Genre1 = new Genre(1, "Пьеса");
        Genre Genre2 = new Genre(2, "Поэма");
        List<Genre> list = List.of(Genre1, Genre2);

        doReturn(list).when(repository).findAll();

        List<String> actList = service.getAllGenres();

        assertThat(actList).containsAll(List.of(Genre1.toString(), Genre2.toString()));
    }

}