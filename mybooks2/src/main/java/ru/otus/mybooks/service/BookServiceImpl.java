package ru.otus.mybooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.*;
import ru.otus.mybooks.exception.BookServiceBookNotFoundException;
import ru.otus.mybooks.exception.BookServiceBookReviewNotFoundException;
import ru.otus.mybooks.repositories.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookDtoMapper bookMapper;
    private final BookReviewsDtoMapper bookReviewsMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> getAllBooks() {
        return repository.findAll().stream()
                .map(bookMapper::getBookDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BookDto getBookById(long id) {
        return bookMapper.getBookDto(repository.findById(id)
                .orElseThrow(() -> new BookServiceBookNotFoundException(id)));
    }

    @Transactional
    @Override
    public BookDto addBook(BookDto bookDto) {
        return bookMapper.getBookDto(repository.save(
                new Book(0, bookDto.getTitle(), getAuthors(bookDto), getGenres(bookDto), List.of())));
    }

    @Transactional
    @Override
    public BookDto editBook(BookDto bookDto) {
        var book = repository.findById(bookDto.getId())
                .orElseThrow(() -> new BookServiceBookNotFoundException(bookDto.getId()));
        var newAuthors = getAuthors(bookDto).stream()
                .filter(a -> !book.getAuthors().contains(a)).collect(Collectors.toList());
        var newGenres = getGenres(bookDto).stream()
                .filter(g -> !book.getGenres().contains(g)).collect(Collectors.toList());
        book.getAuthors().addAll(newAuthors);
        book.getGenres().addAll(newGenres);
        book.setTitle(bookDto.getTitle());
        return bookMapper.getBookDto(repository.save(book));
    }

    @Transactional
    @Override
    public void removeBook(long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else
            throw new BookServiceBookNotFoundException(id);
    }

    @Transactional
    @Override
    public BookReviewsDto addBookReview(long bookId, ReviewDto reviewDto) {
        var book = repository.findById(bookId).orElseThrow(() -> new BookServiceBookNotFoundException(bookId));
        book.getReviews().add(new Review(0L, reviewDto.getText()));
        repository.save(book);
        return bookReviewsMapper.getBookReviewsDto(book);
    }

    @Transactional
    @Override
    public void removeBookReview(long bookId, long reviewId) {
        var book = repository.findById(bookId).orElseThrow(() -> new BookServiceBookNotFoundException(bookId));
        var review = book.getReviews().stream()
                .filter(r -> r.getId() == reviewId)
                .findFirst()
                .orElseThrow(() -> new BookServiceBookReviewNotFoundException(reviewId));
        book.getReviews().remove(review);
        repository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public BookReviewsDto getBookReviews(long bookId) {
        var book = repository.findById(bookId).orElseThrow(() -> new BookServiceBookNotFoundException(bookId));
        return bookReviewsMapper.getBookReviewsDto(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookReviewsDto> getAllBookReviews() {
        return repository.findAll().stream().map(bookReviewsMapper::getBookReviewsDto).collect(Collectors.toList());
    }

    private List<Genre> getGenres(BookDto bookDto) {
        return bookDto.getGenres().isEmpty() ? List.of() :
                bookDto.getGenres().stream()
                        .map(g -> new Genre(0L, g))
                        .map(genreService::addGenre)
                        .collect(Collectors.toList());
    }

    private List<Author> getAuthors(BookDto bookDto) {
        return bookDto.getAuthors().isEmpty() ? List.of() :
                bookDto.getAuthors().stream()
                        .map(a -> new Author(0L, a))
                        .map(authorService::addAuthor)
                        .collect(Collectors.toList());
    }
}
