package ru.otus.mybooks.dto;

import ru.otus.mybooks.domain.Book;

public class BookDto {
    private final long num;
    private final String title;
    private final String author;
    private final String genre;

    public BookDto(Book book) {
        this.num = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor().toString();
        this.genre = book.getGenre().toString();
    }

    @Override
    public String toString() {
        return String.format("%d. %s, %s, %s", num, title, author, genre);
    }
}
