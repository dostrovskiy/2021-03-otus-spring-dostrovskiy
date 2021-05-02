package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;
import ru.otus.mybooks.exception.BookServiceBookNotFoundException;
import ru.otus.mybooks.exception.BookServiceBookReviewNotFoundException;
import ru.otus.mybooks.repositories.BookRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;

    private List<Genre> addGenres(BookDto bookDto) {
        return bookDto.getGenres().isEmpty() ? List.of() :
                bookDto.getGenres().stream()
                        .map(g -> new Genre(0, g))
                        .map(genreService::addGenre)
                        .collect(Collectors.toList());
    }

    private List<Author> addAuthors(BookDto bookDto) {
        return bookDto.getAuthors().isEmpty() ? List.of() :
                bookDto.getAuthors().stream()
                        .map(a -> new Author(0, a))
                        .map(authorService::addAuthor)
                        .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookDto> getAllBooks() {
        return repository.findAll().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Book addBook(BookDto bookDto) {
        return repository.save(new Book(0, bookDto.getTitle(), addAuthors(bookDto), addGenres(bookDto), List.of()));
    }


    @Transactional
    @Override
    public Book editBook(BookDto bookDto) {
        Book book = repository.findById(bookDto.getNum())
                .orElseThrow(() -> new BookServiceBookNotFoundException(bookDto.getNum()));
        book.setAuthors(addAuthors(bookDto));
        book.setGenres(addGenres(bookDto));
        book.setTitle(bookDto.getTitle());
        return repository.save(book);
    }

    @Transactional
    @Override
    public void removeBook(long bookNum) {
        repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        repository.deleteById(bookNum);
    }

    @Transactional
    @Override
    public void addBookAuthor(long bookNum, Author author) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        book.getAuthors().add(authorService.addAuthor(author));
        repository.save(book);
    }

    @Transactional
    @Override
    public void addBookGenre(long bookNum, Genre genre) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        book.getGenres().add(genreService.addGenre(genre));
        repository.save(book);
    }

    @Transactional
    @Override
    public void addBookReview(long bookNum, Review review) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        book.getReviews().add(review);
        repository.save(book);
    }

    @Transactional
    @Override
    public void removeBookReview(long bookNum, long reviewId) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        Review review = book.getReviews().stream()
                .filter(r -> r.getId() == reviewId)
                .findFirst()
                .orElseThrow(() -> new BookServiceBookReviewNotFoundException(reviewId));
        book.getReviews().remove(review);
        repository.save(book);
    }

    @Transactional
    @Override
    public void editBookReview(long bookNum, Review review) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        book.getReviews().stream()
                .filter(r -> r.getId() == review.getId())
                .findFirst()
                .orElseThrow(() -> new BookServiceBookReviewNotFoundException(review.getId()))
                .setText(review.getText());
        repository.save(book);
    }

    @Transactional
    @Override
    public BookReviewsDto getBookReviewsByNum(long bookNum) {
        Book book = repository.findById(bookNum).orElseThrow(() -> new BookServiceBookNotFoundException(bookNum));
        return new BookReviewsDto(book);
    }

    @Transactional
    @Override
    public List<BookReviewsDto> getAllBookReviews() {
        return repository.findAll().stream().map(BookReviewsDto::new).collect(Collectors.toList());
    }
}
