package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.mybooks.config.AppConfig;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.dto.BookDto;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class MyBooksShell {
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final MessageSource ms;
    private final AppConfig cfg;

    private static final Logger logger = LoggerFactory.getLogger(MyBooksShell.class);

    @ShellMethod(key = {"books", "b"}, value = "Display the list of all books: <Num>. <Title>, <Author>, <Genre>")
    public List<String> displayAllBooks() {
        try {
            return bookService.getAllBooks().stream().map(BookDto::toString).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return List.of(ms.getMessage("books.getting.error", null, cfg.getLocale()));
    }

    @ShellMethod(key = {"add-book", "ab"}, value = "Add a book to storage: ab \"Title\" \"Author\" \"Genre\" (Author and Genre are optional)")
    public String addBook(@ShellOption String bookTitle,
                          @ShellOption(defaultValue = "") String authorName,
                          @ShellOption(defaultValue = "") String genreName) {
        try {
            Genre genre = genreName.isEmpty() ?
                    Genre.EMPTY_GENRE :
                    genreService.addGenre(new Genre(genreName));
            Author author = authorName.isEmpty() ?
                    Author.EMPTY_AUTHOR :
                    authorService.addAuthor(new Author(authorName));
            BookDto book = bookService.addBook(new Book(bookTitle, author, genre));
            return ms.getMessage("book.added.successfully", new String[]{book.toString()}, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"edit-book", "eb"}, value = "Edit a book by num: eb Num \"Title\" \"Author\" \"Genre\" (Author and Genre are optional)")
    public String editBook(@ShellOption long bookNum,
                           @ShellOption String bookTitle,
                           @ShellOption(defaultValue = "") String authorName,
                           @ShellOption(defaultValue = "") String genreName) {
        try {
            Genre genre = genreName.isEmpty() ?
                    Genre.EMPTY_GENRE :
                    genreService.addGenre(new Genre(genreName));
            Author author = authorName.isEmpty() ?
                    Author.EMPTY_AUTHOR :
                    authorService.addAuthor(new Author(authorName));
            BookDto book = bookService.saveBook(new Book(bookNum, bookTitle, author, genre));
            return ms.getMessage("book.edited.successfully", new String[]{book.toString()}, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.editing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"remove-book", "rb"}, value = "Remove a book from storage by num: rb Num")
    public String removeBook(@ShellOption long bookNum) {
        try {
            bookService.removeBook(bookNum);
            return ms.getMessage("book.removed.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.removing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"authors", "a"}, value = "Display the list of all authors")
    public List<String> displayAllAuthors() {
        try {
            return authorService.getAllAuthors();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return List.of(ms.getMessage("authors.getting.error", null, cfg.getLocale()));
    }

    @ShellMethod(key = {"add-author", "aa"}, value = "Add an author to storage: aa \"Author\"")
    public String addAuthor(@ShellOption String authorName) {
        try {
            Author author = authorService.addAuthor(new Author(authorName));
            return ms.getMessage("author.added.successfully", new String[]{author.toString()}, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("author.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"genres", "g"}, value = "Display the list of all genres")
    public List<String> displayAllGenres() {
        try {
            return genreService.getAllGenres();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return List.of(ms.getMessage("genres.getting.error", null, cfg.getLocale()));
    }

    @ShellMethod(key = {"add-genre", "ag"}, value = "Add a genre to storage: ag \"Genre\"")
    public String addGenre(@ShellOption String genreName) {
        try {
            Genre genre = genreService.addGenre(new Genre(genreName));
            return ms.getMessage("genre.added.successfully", new String[]{genre.toString()}, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("genre.adding.error", null, cfg.getLocale());
    }
}
