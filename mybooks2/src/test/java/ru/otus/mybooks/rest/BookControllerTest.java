package ru.otus.mybooks.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.mybooks.domain.Review;
import ru.otus.mybooks.dto.BookDto;
import ru.otus.mybooks.dto.BookReviewsDto;
import ru.otus.mybooks.dto.ReviewDto;
import ru.otus.mybooks.exception.BookServiceBookNotFoundException;
import ru.otus.mybooks.exception.BookServiceBookReviewNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Класс BookController должен ")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("вернуть все данные")
    void shouldGetBooksAllInfo() throws Exception {
        var dto1 = new BookReviewsDto("1. На всякого мудреца довольно простоты; Островский А.Н.; Пьеса",
                List.of(new Review(1, "Прекрасная пьеса!!"),
                        new Review(2, "Хм.. ну ок."),
                        new Review(3, "Не читал...")));
        var dto2 = new BookReviewsDto("2. Война и мир", List.of());
        var expList = List.of(dto1, dto2);

        mvc.perform(get("/mybooks/books-all-info")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expList)));
    }

    @Test
    @DisplayName("вернуть все книги")
    void shouldGetBooks() throws Exception {
        var dto1 = new BookDto(1, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"));
        var dto2 = new BookDto(2, "Война и мир", List.of(), List.of());
        List<BookDto> expList = List.of(dto1, dto2);

        mvc.perform(get("/mybooks/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expList)));
    }

    @Test
    @DisplayName("вернуть книгу по идентификатору")
    void shouldGetBook() throws Exception {
        var expBook = new BookDto(1, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"));

        mvc.perform(get("/mybooks/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("добавить книгу")
    void shouldAddBook() throws Exception {
        var book = new BookDto(0, "Deadline. Pоман об управлении проектами",
                List.of("Том Демарко", "Тимоти Листер"), List.of("Роман", "ИТ"));
        var expBook = new BookDto(3, "Deadline. Pоман об управлении проектами",
                List.of("Том Демарко", "Тимоти Листер"), List.of("Роман", "ИТ"));

        mvc.perform(post("/mybooks/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("отредактировать книгу")
    void editBook() throws Exception {
        var originalBook = new BookDto(2, "Война и мир", List.of(), List.of());
        var editedBook = new BookDto(2, "Война и мир",
                List.of("Лев Толстой"), List.of("Роман-эпопея"));

        mvc.perform(get("/mybooks/books/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(originalBook)));

        mvc.perform(put("/mybooks/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(editedBook)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(editedBook)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("удалить книгу")
    void shouldRemoveBook() throws Exception {
        var expBook = new BookDto(1, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"));

        mvc.perform(get("/mybooks/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));

        mvc.perform(delete("/mybooks/books/1")).andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BookServiceBookNotFoundException));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("добавить отзыв о книге")
    void shouldAddBookReview() throws Exception {
        var expBook = new BookReviewsDto("1. На всякого мудреца довольно простоты; Островский А.Н.; Пьеса",
                List.of(new Review(1, "Прекрасная пьеса!!"),
                        new Review(2, "Хм.. ну ок."),
                        new Review(3, "Не читал..."),
                        new Review(4, "Не люблю читать.")));
        var reviewDto = new ReviewDto(0L, "Не люблю читать.");

        mvc.perform(post("/mybooks/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("удалить отзыв о книге")
    void shouldRemoveBookReview() throws Exception {
        var originalBook = new BookReviewsDto("1. На всякого мудреца довольно простоты; Островский А.Н.; Пьеса",
                List.of(new Review(1, "Прекрасная пьеса!!"),
                        new Review(2, "Хм.. ну ок."),
                        new Review(3, "Не читал...")));
        var expBook = new BookReviewsDto("1. На всякого мудреца довольно простоты; Островский А.Н.; Пьеса",
                List.of(new Review(1, "Прекрасная пьеса!!"),
                        new Review(3, "Не читал...")));

        mvc.perform(get("/mybooks/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(originalBook)));

        mvc.perform(delete("/mybooks/books/1/reviews/2")).andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1/reviews").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DisplayName("вернуть отзывы о книге по ее идентификатору")
    void shouldGetBookReviews() throws Exception {
        var expBook = new BookReviewsDto("1. На всякого мудреца довольно простоты; Островский А.Н.; Пьеса",
                List.of(new Review(1, "Прекрасная пьеса!!"), new Review(2, "Хм.. ну ок."),
                        new Review(3, "Не читал...")));

        mvc.perform(get("/mybooks/books/1/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DisplayName("вернуть ошибку, если книга не найдена")
    void shouldHandleBookNotFound() throws Exception {
        mvc.perform(get("/mybooks/books/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BookServiceBookNotFoundException));
    }

    @Test
    @DisplayName("вернуть ошибку, если отзыв не найден")
    void shouldHandleBookReviewNotFound() throws Exception {
        mvc.perform(delete("/mybooks/books/1/reviews/55"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BookServiceBookReviewNotFoundException));
    }
}