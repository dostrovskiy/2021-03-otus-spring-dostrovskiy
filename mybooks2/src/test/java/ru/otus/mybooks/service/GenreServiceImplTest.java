package ru.otus.mybooks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.GenreDto;
import ru.otus.mybooks.dtoconverters.AuthorDtoConverter;
import ru.otus.mybooks.dtoconverters.BookDtoConverterImpl;
import ru.otus.mybooks.dtoconverters.GenreDtoConverter;
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
    private GenreDtoConverter dtoConverter;

    @BeforeEach
    void setUp() {
        service = new GenreServiceImpl(repository, dtoConverter);
    }

    @Test
    @DisplayName("добавлять жанр")
    void shouldAddGenre() {
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
        Genre genre1 = new Genre(1, "Пьеса");
        Genre genre2 = new Genre(2, "Поэма");
        List<Genre> list = List.of(genre1, genre2);
        GenreDto genreDto1 = new GenreDto(1, "Пьеса");
        GenreDto genreDto2 = new GenreDto(2, "Поэма");
        List<GenreDto> expDto = List.of(genreDto1, genreDto2);

        doReturn(list).when(repository).findAll();
        doReturn(genreDto1).when(dtoConverter).getGenreDto(genre1);
        doReturn(genreDto2).when(dtoConverter).getGenreDto(genre2);

        List<GenreDto> actList = service.getAllGenres();

        assertThat(actList).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expDto);
    }

}