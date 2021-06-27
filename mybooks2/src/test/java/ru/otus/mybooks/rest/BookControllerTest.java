package ru.otus.mybooks.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Класс BookController должен ")
class BookControllerTest {
    public static final String READER_CREDENTIALS = "cmVhZGVyOnBhc3M=";
    public static final String ADMIN_CREDENTIALS = "YWRtaW46cGFzcw==";
    public static final String INVALID_SIGNATURE_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlIjoiUk9MRV9BRE1JTiIsImlzcyI6InNlbGYiLCJleHAiOjE2MjQ3NDYyMDAsImlhdCI6MTYyNDcxMDIwMH0.dgtGxkeyuYUGdfIuIIbPorXqtZ3O1_pdj4rtlFfoiG3_rCqSOBv9v-VVwDBpdtHv4mo94gvSclfgm0TAhzaSZpEd--p1AjfzyqJmRRJfm5xA4CAeAzKVw3kEKfyeCsjBnBzFno0gCMCU4ey5PmaJ79WVutB4KIdBLBbr-fnDwsntNlsJ6k8XKC9_nrDdFx55dPisM0df3R501YxNHU9jH-erx6KzgR7UfQAMN1KbJyFfZczLuGVFR4M_LRYiDAGu8VpJhCcADxqT6jSwLbwGRPOBGqou8BuiDRY4uoAty8HviyyrK2KKHqOluOSd1gCzG_nBzJsWVyRFKe4lYN_C4g";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private final Map<String, String> tokens = new HashMap<>();

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
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
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
        var expList = List.of(dto1, dto2);

        mvc.perform(get("/mybooks/books")
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
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
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
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

        mvc.perform(post("/mybooks/books")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/3")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
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
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(originalBook)));

        mvc.perform(put("/mybooks/books/")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(editedBook)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/2")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
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
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));

        mvc.perform(delete("/mybooks/books/1")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
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
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1/reviews")
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
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
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(originalBook)));

        mvc.perform(delete("/mybooks/books/1/reviews/2")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS)))
                .andExpect(status().isOk());

        mvc.perform(get("/mybooks/books/1/reviews")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
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
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expBook)));
    }

    @Test
    @DisplayName("вернуть ошибку, если книга не найдена")
    void shouldHandleBookNotFound() throws Exception {
        mvc.perform(get("/mybooks/books/55")
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BookServiceBookNotFoundException));
    }

    @Test
    @DisplayName("вернуть ошибку, если отзыв не найден")
    void shouldHandleBookReviewNotFound() throws Exception {
        mvc.perform(delete("/mybooks/books/1/reviews/55")
                .header("Authorization", "Bearer " + getToken(ADMIN_CREDENTIALS)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BookServiceBookReviewNotFoundException));
    }

    @Test
    @DisplayName("вернуть ошибку 401, если токен не передан")
    void shouldHandleNoToken() throws Exception {
        mvc.perform(get("/mybooks/books-all-info")).andExpect(status().is(401));
    }

    @Test
    @DisplayName("вернуть ошибку 401, если передан неверный токен")
    void shouldHandleInvalidSignatureToken() throws Exception {
        mvc.perform(get("/mybooks/books-all-info")
                .header("Authorization", "Bearer " + INVALID_SIGNATURE_TOKEN))
                .andExpect(status().is(401))
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("WWW-Authenticate")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getHeader("WWW-Authenticate").contains("Invalid signature")));
    }

    @Test
    @DisplayName("вернуть ошибку 403, если передан токен с недостаточными правами")
    void shouldHandleLowerPrivilegesToken() throws Exception {
        mvc.perform(get("/mybooks/books-all-info")
                .header("Authorization", "Bearer " + getToken(READER_CREDENTIALS)))
                .andExpect(status().is(403))
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("WWW-Authenticate")))
                .andExpect(result -> assertTrue(
                        result.getResponse().getHeader("WWW-Authenticate").contains("The request requires higher privileges")));
    }

    @SneakyThrows
    private String getToken(String credentials) {
        if (!tokens.containsKey(credentials)) {
            var result = mvc.perform(post("/token")
                    .header("Authorization", "Basic " + credentials))
                    .andExpect(status().isOk())
                    .andReturn();
            tokens.put(credentials, result.getResponse().getContentAsString());
        }
        return tokens.get(credentials);
    }
}