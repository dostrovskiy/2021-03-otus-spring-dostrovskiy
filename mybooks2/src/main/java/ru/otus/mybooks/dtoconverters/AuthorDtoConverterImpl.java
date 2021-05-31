package ru.otus.mybooks.dtoconverters;

import org.springframework.stereotype.Service;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.dto.AuthorDto;

@Service
public class AuthorDtoConverterImpl implements AuthorDtoConverter {

    @Override
    public AuthorDto getAuthorDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
