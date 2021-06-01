package ru.otus.mybooks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;
import ru.otus.mybooks.dto.GenreDtoMapper;
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
    @Mock
    private GenreDtoMapper dtoMapper;

    @BeforeEach
    void setUp() {
        service = new GenreServiceImpl(repository, dtoMapper);
    }

    @Test
    @DisplayName("добавлять жанр")
    void shouldAddGenre() {
        var Genre = new Genre(0, "Поэма");
        var expGenre = new Genre(2, "Поэма");

        doReturn(List.of()).when(repository).findByName("Поэма");
        doReturn(expGenre).when(repository).save(Genre);

        var actGenre = service.addGenre(Genre);

        assertThat(actGenre).usingRecursiveComparison().isEqualTo(expGenre);
    }

    @Test
    @DisplayName("получать все жанры")
    void shouldGetAllGenres() {
        var genre1 = new Genre(1, "Пьеса");
        var genre2 = new Genre(2, "Поэма");
        var list = List.of(genre1, genre2);
        var genreDto1 = new GenreDto(1, "Пьеса");
        var genreDto2 = new GenreDto(2, "Поэма");
        var expDto = List.of(genreDto1, genreDto2);

        doReturn(list).when(repository).findAll();
        doReturn(genreDto1).when(dtoMapper).getGenreDto(genre1);
        doReturn(genreDto2).when(dtoMapper).getGenreDto(genre2);

        var actList = service.getAllGenres();

        assertThat(actList).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expDto);
    }

}