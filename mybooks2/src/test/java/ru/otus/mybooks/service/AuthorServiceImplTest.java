package ru.otus.mybooks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.dto.AuthorDto;
import ru.otus.mybooks.dto.AuthorDtoMapper;
import ru.otus.mybooks.repositories.AuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс AuthorServiceImpl должен")
class AuthorServiceImplTest {

    private AuthorService service;
    @Mock
    private AuthorRepository repository;
    @Mock
    private AuthorDtoMapper dtoMapper;

    @Test
    @DisplayName("добавлять автора")
    void shouldAddAuthor() {
        service = new AuthorServiceImpl(repository, dtoMapper);
        var author = new Author(0, "Пушкин А.С.");
        var expAuthor = new Author(2, "Пушкин А.С.");

        doReturn(List.of()).when(repository).findByName("Пушкин А.С.");
        doReturn(expAuthor).when(repository).save(author);

        var actAuthor = service.addAuthor(author);

        assertThat(actAuthor).usingRecursiveComparison().isEqualTo(expAuthor);
    }

    @Test
    @DisplayName("получать всех авторов")
    void shouldGetAllAuthors() {
        service = new AuthorServiceImpl(repository, dtoMapper);
        var author1 = new Author(1, "Пушкин А.С.");
        var author2 = new Author(2, "Лермонтов М.Ю.");
        var list = List.of(author1, author2);
        var authorDto1 = new AuthorDto(1, "Пушкин А.С.");
        var authorDto2 = new AuthorDto(2, "Лермонтов М.Ю.");
        var expList = List.of(authorDto1, authorDto2);

        doReturn(list).when(repository).findAll();
        doReturn(authorDto1).when(dtoMapper).getAuthorDto(author1);
        doReturn(authorDto2).when(dtoMapper).getAuthorDto(author2);

        var actList = service.getAllAuthors();

        assertThat(actList).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expList);
    }
}