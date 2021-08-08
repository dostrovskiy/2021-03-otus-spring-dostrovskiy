package ru.otus.mybooks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.*;
import ru.otus.mybooks.exception.BookServiceBookNotFoundException;
import ru.otus.mybooks.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс BookServiceImpl должен")
class BookServiceImplTest {
    private BookService service;
    @Mock
    private BookRepository repository;
    @Mock
    private AuthorService authorService;
    @Mock
    private GenreService genreService;
    @Mock
    private BookDtoMapper bookMapper;
    @Mock
    private BookReviewsDtoMapper bookReviewsMapper;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(repository, authorService, genreService, bookMapper, bookReviewsMapper);
    }

    @Test
    @DisplayName("получать все книги")
    void shouldGetAllBooks() {
        var author1 = new Author(1, "Пушкин А.С.");
        var author2 = new Author(2, "Лермонтов М.Ю.");
        var genre = new Genre(1, "Поэма");
        var book1 = new Book(1, "Руслан и Людмила", List.of(author1), List.of(genre), List.of(), "123-5-456-78908-2");
        var book2 = new Book(2, "Мцыри", List.of(author2), List.of(genre), List.of(), "123-5-456-78905-2");
        var list = List.of(book1, book2);
        var dto1 = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"), "123-5-456-78908-2");
        var dto2 = new BookDto(2, "Мцыри", List.of("Лермонтов М.Ю."), List.of("Поэма"), "123-5-456-78905-2");
        var expList = List.of(dto1, dto2);

        doReturn(list).when(repository).findAll();
        doReturn(dto1).when(bookMapper).getBookDto(book1);
        doReturn(dto2).when(bookMapper).getBookDto(book2);

        var actList = service.getAllBooks();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(expList);
    }

    @Test
    @DisplayName("добавлять новую книгу")
    void shouldAddBook() {
        var author = new Author(0, "Пушкин А.С.");
        var genre = new Genre(0, "Поэма");
        var expBook = new Book(0, "Руслан и Людмила", List.of(author), List.of(genre), List.of(), "123-5-456-78908-2");
        var expBookDto = new BookDto(0, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"), "123-5-456-78908-2");
        var bookCaptor = ArgumentCaptor.forClass(Book.class);
        var authorCaptor = ArgumentCaptor.forClass(Author.class);
        var genreCaptor = ArgumentCaptor.forClass(Genre.class);

        doReturn(author).when(authorService).addAuthor(authorCaptor.capture());
        doReturn(genre).when(genreService).addGenre(genreCaptor.capture());
        doReturn(expBook).when(repository).save(bookCaptor.capture());
        doReturn(expBookDto).when(bookMapper).getBookDto(expBook);

        var actBookDto = service.addBook(expBookDto);

        assertAll(
                () -> assertThat(actBookDto).usingRecursiveComparison().isEqualTo(expBookDto),
                () -> assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(authorCaptor.getValue()).usingRecursiveComparison().isEqualTo(author),
                () -> assertThat(genreCaptor.getValue()).usingRecursiveComparison().isEqualTo(genre)
        );
    }

    @Test
    @DisplayName("редактировать книгу")
    void shouldEditBook() {
        var book = new Book(1, "Руслан", new ArrayList<>(List.of()), new ArrayList<>(List.of()), List.of(), "123-5-456-78908-2");
        var expAuthor = new Author(0, "Пушкин А.С.");
        var expGenre = new Genre(0, "Поэма");
        var expBook = new Book(1, "Руслан и Людмила", List.of(expAuthor), List.of(expGenre), List.of(), "123-5-456-78908-2");
        var expBookDto = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"), "123-5-456-78908-2");
        var bookCaptor = ArgumentCaptor.forClass(Book.class);
        var authorCaptor = ArgumentCaptor.forClass(Author.class);
        var genreCaptor = ArgumentCaptor.forClass(Genre.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(expAuthor).when(authorService).addAuthor(authorCaptor.capture());
        doReturn(expGenre).when(genreService).addGenre(genreCaptor.capture());
        doReturn(expBook).when(repository).save(bookCaptor.capture());
        doReturn(expBookDto).when(bookMapper).getBookDto(expBook);

        var actBookDto = service.editBook(expBookDto);

        assertAll(
                () -> assertThat(actBookDto).usingRecursiveComparison().isEqualTo(expBookDto),
                () -> assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(authorCaptor.getValue()).usingRecursiveComparison().isEqualTo(expAuthor),
                () -> assertThat(genreCaptor.getValue()).usingRecursiveComparison().isEqualTo(expGenre)
        );
    }

    @Test
    @DisplayName("удалять книгу")
    void shouldRemoveBook() {
        doReturn(true).when(repository).existsById(1L);

        service.removeBook(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("добавлять отзыв о книге")
    void shouldAddBookReview() {
        var review = new Review(1L, "Очень интересно!");
        var review2 = new Review(0L, "Самый смешной рассказ, который я читал.");
        var reviewDto = new ReviewDto(0L, "Самый смешной рассказ, который я читал.");
        var book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review)), "123-5-456-78909-2");
        var expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2), "123-5-456-78909-2");
        var bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookReview(1L, reviewDto);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("удалять отзыв о книге")
    void shouldRemoveBookReview() {
        var review = new Review(1L, "Очень интересно!");
        var review2 = new Review(2L, "Самый смешной рассказ, который я читал.");
        var book = new Book(1L, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review, review2)), "123-5-456-78909-2");
        var expBook = new Book(1L, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review2), "123-5-456-78909-2");
        var bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.removeBookReview(1, 1);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("получать комментарии к книге")
    void shouldGetBookReviews() {
        var review = new Review(1, "Очень интересно!");
        var review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        var book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2), "123-5-456-78909-2");
        var bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of(), "123-5-456-78909-2");
        var expBookReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(expBookReviewsDto).when(bookReviewsMapper).getBookReviewsDto(book);

        var actBookReviewsDto = service.getBookReviews(1);

        assertThat(actBookReviewsDto).usingRecursiveComparison().isEqualTo(expBookReviewsDto);
    }

    @Test
    @DisplayName("получать все комментарии")
    void shouldGetAllBookReviews() {
        var review = new Review(1, "Очень интересно!");
        var review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        var book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2), "123-5-456-78909-2");
        var bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of(), "123-5-456-78909-2");
        var bookReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));
        var expList = List.of(bookReviewsDto);

        doReturn(List.of(book)).when(repository).findAll();
        doReturn(bookReviewsDto).when(bookReviewsMapper).getBookReviewsDto(book);

        var actList = service.getAllBookReviews();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(expList);
    }

    @Test
    @DisplayName("выдавать ошибку, если редактируемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfEditingBookNotFoundById() {
        var dto = new BookDto(55, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"), "123-5-456-78908-2");

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThatExceptionOfType(BookServiceBookNotFoundException.class).isThrownBy(() -> service.editBook(dto));
    }
}