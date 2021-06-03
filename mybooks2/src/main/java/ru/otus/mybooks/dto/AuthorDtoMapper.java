package ru.otus.mybooks.dto;

import org.mapstruct.Mapper;
import ru.otus.mybooks.domain.Author;

@Mapper(componentModel = "spring")
public interface AuthorDtoMapper {
    AuthorDto getAuthorDto(Author author);
}
