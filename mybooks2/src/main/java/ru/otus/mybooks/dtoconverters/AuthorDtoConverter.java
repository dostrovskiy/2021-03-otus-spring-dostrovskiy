package ru.otus.mybooks.dtoconverters;

import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.dto.AuthorDto;

public interface AuthorDtoConverter {
    AuthorDto getAuthorDto(Author author);
}
