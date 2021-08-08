package ru.otus.mybooks.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.mybooks.domain.Book;

@Mapper(componentModel = "spring")
public abstract class BookReviewsDtoMapper {
    protected BookDtoMapper bookMapper;

    @Autowired
    public void setBookDtoMapper(BookDtoMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Mapping(target = "bookInfo", expression = "java(bookMapper.getBookDto(book).toString())")
    public abstract BookReviewsDto getBookReviewsDto(Book book);
}
