package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.mybooks.config.AppConfig;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;

import java.util.ArrayList;
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

    @ShellMethod(key = {"show-console", "c"}, value = "Display the console of the H2 DB")
    public void showDBConsole() {
        try {
            Console.main();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @ShellMethod(key = {"show-all-books", "b"}, value = "Display the list of all books: <Num>. <Title>, <Author>, <Genre>")
    public List<String> showAllBooks() {
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
            bookService.addBook(new BookDto(0, bookTitle, List.of(authorName), List.of(genreName)));
            return ms.getMessage("book.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"remove-book"}, value = "Remove a book from storage by num: remove-book Num")
    public String removeBook(@ShellOption long bookNum) {
        try {
            bookService.removeBook(bookNum);
            return ms.getMessage("book.removed.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.removing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"edit-book", "eb"}, value = "Edit a book by num: eb Num \"Title\" \"Author\" \"Genre\" (Author and Genre are optional)")
    public String editBook(@ShellOption long bookNum,
                           @ShellOption String bookTitle,
                           @ShellOption(defaultValue = "") String authorName,
                           @ShellOption(defaultValue = "") String genreName) {
        try {
            bookService.editBook(new BookDto(bookNum, bookTitle, List.of(authorName), List.of(genreName)));
            return ms.getMessage("book.edited.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("book.editing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"add-book-author", "aba"}, value = "Add an author to book: aba BookNum \"Author name\"")
    public String addAuthor(@ShellOption long bookNum,
                            @ShellOption String authorName) {
        try {
            bookService.addBookAuthor(bookNum, new Author(0, authorName));
            return ms.getMessage("author.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("author.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"add-book-genre", "abg"}, value = "Add a genre to book: abg BookNum \"Genre name\"")
    public String addGenre(@ShellOption long bookNum,
                           @ShellOption String genreName) {
        try {
            bookService.addBookGenre(bookNum, new Genre(0, genreName));
            return ms.getMessage("genre.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("genre.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"add-book-review", "abr"}, value = "Add a review to book: abr BookNum \"Review text\"")
    public String addReview(@ShellOption long bookNum,
                            @ShellOption String reviewText) {
        try {
            bookService.addBookReview(bookNum, new Review(0, reviewText));
            return ms.getMessage("review.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("review.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"remove-book-review", "rbr"}, value = "Remove the review from the book: rbr BookNum ReviewID")
    public String removeReview(@ShellOption long bookNum,
                               @ShellOption long reviewId) {
        try {
            bookService.removeBookReview(bookNum, reviewId);
            return ms.getMessage("review.removed.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("review.removing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"edit-book-review", "ebr"}, value = "Edit the review in the book: ebr BookNum ReviewID \"Text\"")
    public String editReview(@ShellOption long bookNum,
                             @ShellOption long reviewId,
                             @ShellOption String reviewText) {
        try {
            bookService.editBookReview(bookNum, new Review(reviewId, reviewText));
            return ms.getMessage("review.edited.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("review.editing.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"show-book-reviews", "br"}, value = "Display the list of reviews of the book by book num: br Num")
    public List<String> showBookReviews(@ShellOption long bookNum) {
        try {
            return bookReviewsLister(bookService.getBookReviewsByNum(bookNum));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return List.of(ms.getMessage("reviews.getting.error", null, cfg.getLocale()));
    }

    @ShellMethod(key = {"show-all-reviews", "r"}, value = "Display the list of all reviews")
    public List<String> showAllReviews() {
        try {
            List<BookReviewsDto> reviews = bookService.getAllBookReviews();
            return reviews.stream()
                    .map(this::bookReviewsLister)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return List.of(ms.getMessage("reviews.getting.error", null, cfg.getLocale()));
    }

    @ShellMethod(key = {"show-all-authors", "a"}, value = "Display the list of all authors")
    public List<String> showAllAuthors() {
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
            authorService.addAuthor(new Author(0, authorName));
            return ms.getMessage("author.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("author.adding.error", null, cfg.getLocale());
    }

    @ShellMethod(key = {"show-all-genres", "g"}, value = "Display the list of all genres")
    public List<String> showAllGenres() {
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
            genreService.addGenre(new Genre(0, genreName));
            return ms.getMessage("genre.added.successfully", null, cfg.getLocale());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ms.getMessage("genre.adding.error", null, cfg.getLocale());
    }

    private List<String> bookReviewsLister(BookReviewsDto b) {
        List<String> l = new ArrayList<>(List.of(b.getBookInfo()));
        if (b.getReviews().isEmpty()) {
            l.add(ms.getMessage("no.reviews", null, cfg.getLocale()));
        } else {
            l.add(ms.getMessage("reviews.of", null, cfg.getLocale()));
            l.addAll(b.getReviews().stream().map(r -> " - " + r.getText() + " (ID " + r.getId() + ")").collect(Collectors.toList()));
        }
        return l;
    }
}
