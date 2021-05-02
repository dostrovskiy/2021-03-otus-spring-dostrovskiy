package ru.otus.mybooks.service;

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

    @Test
    @DisplayName("получать все книги")
    void shouldGetAllBooks() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        Genre genre = new Genre(1, "Поэма");
        Book book1 = new Book(1, "Руслан и Людмила", List.of(author1), List.of(genre), List.of());
        Book book2 = new Book(2, "Мцыри", List.of(author2), List.of(genre), List.of());
        List<Book> list = List.of(book1, book2);
        BookDto dto1 = new BookDto(book1);
        BookDto dto2 = new BookDto(book2);
        List<BookDto> listDto = List.of(dto1, dto2);

        doReturn(list).when(repository).findAll();

        List<BookDto> actList = service.getAllBooks();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(listDto);
    }

    @Test
    @DisplayName("добавлять новую книгу")
    void shouldAddBook() {
        service = new BookServiceImpl(repository, authorService, genreService);

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
        service = new BookServiceImpl(repository, authorService, genreService);

        Author author = new Author(1, "Гоголь Н.В.");
        Genre genre = new Genre(1, "Пьеса");
        Book book = new Book(1, "Руслан", List.of(author), List.of(genre), List.of());

        Author expAuthor = new Author(0, "Пушкин А.С.");
        Genre expGenre = new Genre(0, "Поэма");
        Book expBook = new Book(1, "Руслан и Людмила", List.of(expAuthor), List.of(expGenre), List.of());

        BookDto dto = new BookDto(1, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        ArgumentCaptor<Genre> genreCaptor = ArgumentCaptor.forClass(Genre.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
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
        service = new BookServiceImpl(repository, authorService, genreService);
        Author author1 = new Author(1, "Пушкин А.С.");
        Author author2 = new Author(2, "Лермонтов М.Ю.");
        Genre genre = new Genre(1, "Поэма");
        Book book1 = new Book(1, "Руслан и Людмила", List.of(author1), List.of(genre), List.of());
        Book book2 = new Book(2, "Мцыри", List.of(author2), List.of(genre), List.of());
        List<Book> list = new ArrayList<>(List.of(book1, book2));

        doReturn(Optional.of(book1)).when(repository).findById(anyLong());
        doAnswer(i -> list.remove(0)).when(repository).deleteById(1);

        service.removeBook(1);

        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list).usingFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(List.of(book2))
        );
    }

    @Test
    @DisplayName("добавлять автора к книге")
    void shouldAddBookAuthor() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Author author = new Author(1, "Том Демарко");
        Author author2 = new Author(2, "Тимоти Листер");
        Book book = new Book(1, "Deadline. Pоман об управлении проектами",
                new ArrayList<Author>(List.of(author)), List.of(), List.of());
        Book expBook = new Book(1, "Deadline. Pоман об управлении проектами",
                List.of(author, author2), List.of(), List.of());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
        doReturn(author2).when(authorService).addAuthor(author2);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookAuthor(1, author2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("добавлять жанр к книге")
    void shouldAddBookGenre() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Genre genre = new Genre(1, "Рассказ");
        Genre genre2 = new Genre(2, "Детская литература");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), new ArrayList<Genre>(List.of(genre)), List.of());
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(genre, genre2), List.of());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
        doReturn(genre2).when(genreService).addGenre(genre2);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookGenre(1, genre2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("добавлять комментарий к книге")
    void shouldAddBookReview() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.addBookReview(1, review2);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("удалять комментарий к книге")
    void shouldRemoveBookReview() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review, review2)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review2));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.removeBookReview(1, 1);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("редактировать комментарий к книге")
    void shouldEditBookReview() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Review review = new Review(1, "Очень интересно!");
        Review newReview = new Review(1, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), new ArrayList<Review>(List.of(review)));
        Book expBook = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(newReview));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        doReturn(Optional.of(book)).when(repository).findById(1);
        doReturn(book).when(repository).save(bookCaptor.capture());

        service.editBookReview(1, newReview);

        assertThat(bookCaptor.getValue()).usingRecursiveComparison().isEqualTo(expBook);
    }

    @Test
    @DisplayName("получать комментарии к книге")
    void shouldGetBookReviewsByNum() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        BookReviewsDto expBookReviewsDto = new BookReviewsDto(book);

        doReturn(Optional.of(book)).when(repository).findById(1);

        BookReviewsDto actBookReviewsDto = service.getBookReviewsByNum(1);

        assertThat(actBookReviewsDto).usingRecursiveComparison().isEqualTo(expBookReviewsDto);
    }

    @Test
    @DisplayName("получать все комментарии")
    void shouldGetAllBookReviews() {
        service = new BookServiceImpl(repository, authorService, genreService);
        Review review = new Review(1, "Очень интересно!");
        Review review2 = new Review(2, "Самый смешной рассказ, который я читал.");
        Book book = new Book(1, "Пожар во флигеле, или Подвиг во льдах...",
                List.of(), List.of(), List.of(review, review2));
        List<BookReviewsDto> expList = List.of(new BookReviewsDto(book));

        doReturn(List.of(book)).when(repository).findAll();

        List<BookReviewsDto> actList = service.getAllBookReviews();

        assertThat(actList).usingFieldByFieldElementComparator().isEqualTo(expList);
    }

    @Test
    @DisplayName("выдавать ошибку, если редактируемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfEditingBookNotFoundById() {
        service = new BookServiceImpl(repository, authorService, genreService);
        BookDto dto = new BookDto(55, "Руслан и Людмила", List.of("Пушкин А.С."), List.of("Поэма"));

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThatExceptionOfType(BookServiceBookNotFoundException.class).isThrownBy(() -> service.editBook(dto));
    }

    @Test
    @DisplayName("выдавать ошибку, если удаляемая книга не найдена по идентификатору")
    void shouldThrowExceptionIfRemovingBookNotFoundById() {
        service = new BookServiceImpl(repository, authorService, genreService);

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThatExceptionOfType(BookServiceBookNotFoundException.class).isThrownBy(() -> service.removeBook(55));
    }
}