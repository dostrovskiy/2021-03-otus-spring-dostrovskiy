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
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;
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
    private BookDtoConverter bookDtoConverter;
    @Mock
    private BookReviewsDtoConverter reviewsDtoConverter;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(repository, authorService, genreService, bookDtoConverter, reviewsDtoConverter);
    }

    @Test
    @DisplayName("получать все книги")
    void shouldGetAllBooks() {
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        Genre genre = new Genre(1, "Поэма");
        Book book1 = new Book(1, "Руслан и Людмила", List.of(author1), List.of(genre), List.of());
        Book book2 = new Book(2, "Мцыри", List.of(author2), List.of(genre), List.of());
        List<Book> list = List.of(book1, book2);
        BookDto dto1 = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));
        BookDto dto2 = new BookDto(2, "Мцыри", List.of("Лермонтов М.Ю."), List.of("Поэма"));
        List<BookDto> listDto = List.of(dto1, dto2);

        doReturn(list).when(repository).findAll();
        doReturn(dto1).when(bookDtoConverter).getBookDto(book1);
        doReturn(dto2).when(bookDtoConverter).getBookDto(book2);

        List<BookDto> actList = service.getAllBooks();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(listDto);
    }

    @Test
    @DisplayName("добавлять новую книгу")
    void shouldAddBook() {
        Author author = new Author(0, "Пушкин А.С.");
        Genre genre = new Genre(0, "Поэма");
        Book expBook = new Book(0, "Руслан и Людмила", List.of(author), List.of(genre), List.of());
        BookDto dto = new BookDto(0, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        ArgumentCaptor<Genre> genreCaptor = ArgumentCaptor.forClass(Genre.class);

        doReturn(author).when(authorService).addAuthor(authorCaptor.capture());
        doReturn(genre).when(genreService).addGenre(genreCaptor.capture());
        doReturn(expBook).when(repository).save(bookCaptor.capture());

        Book actBook = service.addBook(dto);

        assertAll(
                () -> assertThat(actBook).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(authorCaptor.getValue()).usingRecursiveComparison().isEqualTo(author),
                () -> assertThat(genreCaptor.getValue()).usingRecursiveComparison().isEqualTo(genre)
        );
    }

    @Test
    @DisplayName("сохранять отредактированную книгу")
    void shouldEditBook() {
        Book book = new Book(1, "Руслан", new ArrayList<>(List.of()), new ArrayList<>(List.of()), List.of());

        Author expAuthor = new Author(0, "Пушкин А.С.");
        Genre expGenre = new Genre(0, "Поэма");
        Book expBook = new Book(1, "Руслан и Людмила", List.of(expAuthor), List.of(expGenre), List.of());

        BookDto dto = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        ArgumentCaptor<Genre> genreCaptor = ArgumentCaptor.forClass(Genre.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(expAuthor).when(authorService).addAuthor(authorCaptor.capture());
        doReturn(expGenre).when(genreService).addGenre(genreCaptor.capture());
        doReturn(expBook).when(repository).save(bookCaptor.capture());

        Book actBook = service.editBook(dto);

        assertAll(
                () -> assertThat(actBook).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook),
                () -> assertThat(authorCaptor.getValue()).usingRecursiveComparison().isEqualTo(expAuthor),
                () -> assertThat(genreCaptor.getValue()).usingRecursiveComparison().isEqualTo(expGenre)
        );
    }

    @Test
    @DisplayName("удалять книгу")
    void shouldRemoveBook() {
        service.removeBook(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("добавлять автора к книге")
    void shouldAddBookAuthor() {
        Author author = new Author(1, "Том Демарко");
        Author author2 = new Author(2, "Тимоти Листер");
        Book book = new Book(1, "Deadline. Pоман об управлении проектами",
                new ArrayList<Author>(List.of(author)), List.of(), List.of());
        Book expBook = new Book(1, "Deadline. Pоман об управлении проектами",
                List.of(author, author2), List.of(), List.of());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(author2).when(authorService).addAuthor(author2);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookAuthor(1, author2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("добавлять жанр к книге")
    void shouldAddBookGenre() {
        Genre genre = new Genre(1, "Рассказ");
        Genre genre2 = new Genre(2, "Детская литература");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), new ArrayList<Genre>(List.of(genre)), List.of());
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(genre, genre2), List.of());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(genre2).when(genreService).addGenre(genre2);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookGenre(1, genre2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("добавлять комментарий к книге")
    void shouldAddBookReview() {
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookReview(1, review2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("удалять комментарий к книге")
    void shouldRemoveBookReview() {
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review, review2)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review2));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.removeBookReview(1, 1);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("редактировать комментарий к книге")
    void shouldEditBookReview() {
        Review review = new Review(1, "Очень интересно!");
        Review newReview = new Review(1, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(newReview));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.editBookReview(1, newReview);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("получать комментарии к книге")
    void shouldGetBookReviewsByNum() {
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        BookDto bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of());
        BookReviewsDto expBookReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));

        doReturn(Optional.of(book)).when(repository).findById(1L);
        doReturn(expBookReviewsDto).when(reviewsDtoConverter).getBookReviews(book);

        BookReviewsDto actBookReviewsDto = service.getBookReviewsByNum(1);

        assertThat(actBookReviewsDto).usingRecursiveComparison().isEqualTo(expBookReviewsDto);
    }

    @Test
    @DisplayName("получать все комментарии")
    void shouldGetAllBookReviews() {
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        BookDto bookDto = new BookDto(1, "Пожар во флигеле, или Подвиг во льдах...", List.of(), List.of());
        BookReviewsDto bookReviewsDto = new BookReviewsDto(bookDto.toString(), List.of(review, review2));
        List<BookReviewsDto> expList = List.of(bookReviewsDto);

        doReturn(List.of(book)).when(repository).findAll();
        doReturn(bookReviewsDto).when(reviewsDtoConverter).getBookReviews(book);

        List<BookReviewsDto> actList = service.getAllBookReviews();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(expList);
    }

    @Test
    @DisplayName("выдавать ошибку, если редактируемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfEditingBookNotFoundById() {
        BookDto dto = new BookDto(55, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThatExceptionOfType(BookServiceBookNotFoundException.class).isThrownBy(() -> service.editBook(dto));
    }
}