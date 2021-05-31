package ru.otus.mybooks.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.mybooks.config.AppConfig;
import ru.otus.mybooks.dto.AuthorDto;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;
import ru.otus.mybooks.dto.ReviewDto;
import ru.otus.mybooks.exception.BookServiceBookNotFoundException;
import ru.otus.mybooks.exception.BookServiceBookReviewNotFoundException;
import ru.otus.mybooks.service.BookService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final MessageSource ms;
    private final AppConfig cfg;

    @GetMapping("/mybooks/books-all-info")
    public List<BookReviewsDto> getBooksAllInfo() {
        return bookService.getAllBookReviews();
    }

    @GetMapping("/mybooks/books")
    public List<BookDto> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/mybooks/books/{id}")
    public BookDto getBook(@PathVariable("id") long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/mybooks/books")
    public BookDto addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/mybooks/books")
    public BookDto editBook(@RequestBody BookDto book) {
        return bookService.editBook(book);
    }

    @DeleteMapping("/mybooks/books/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.removeBook(id);
    }

    @GetMapping("/mybooks/books/{book-id}/reviews")
    public BookReviewsDto getBookReviews(@PathVariable("book-id") long bookId) {
        return bookService.getBookReviews(bookId);
    }

    @PostMapping("/mybooks/books/{book-id}/reviews")
    public BookReviewsDto addBookReview(@PathVariable("book-id") long id, @RequestBody ReviewDto reviewDto) {
        return bookService.addBookReview(id, reviewDto);
    }

    @DeleteMapping("/mybooks/books/{book-id}/reviews/{id}")
    public void deleteBookReview(@PathVariable("book-id") long bookId, @PathVariable("id") long id) {
        bookService.removeBookReview(bookId, id);
    }

    @ExceptionHandler(BookServiceBookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFound(BookServiceBookNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BookServiceBookReviewNotFoundException.class)
    public ResponseEntity<String> handleBookReviewNotFound(BookServiceBookReviewNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
