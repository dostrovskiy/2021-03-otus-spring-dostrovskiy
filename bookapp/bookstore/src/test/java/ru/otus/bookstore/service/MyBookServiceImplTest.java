package ru.otus.bookstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ru.otus.bookstore.dto.BookDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс MyBookServiceImpl должен")
class MyBookServiceImplTest {
    private MyBookService myBookService;
    private CircuitBreakerFactory circuitBreakerFactory;
    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("получить книги")
    void shouldGetBooks() {
        circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();
        myBookService = new MyBookServiceImpl(circuitBreakerFactory, restTemplate);
        ReflectionTestUtils.setField(myBookService, "savedToken", "fake token");
        var books = new BookDto[]{new BookDto(1L, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"), "123-5-456-78901-2")};
        var response = new ResponseEntity<BookDto[]>(books, HttpStatus.OK);
        var expBooks = List.of(new BookDto(1L, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"), "123-5-456-78901-2"));

        doReturn(response).when(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(BookDto[].class));

        var actBooks = myBookService.getBooks();

        assertThat(actBooks).usingRecursiveFieldByFieldElementComparator().isEqualTo(expBooks);
    }

    @Test
    @DisplayName("получить токен")
    void shouldGetToken() {
        circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();
        myBookService = new MyBookServiceImpl(circuitBreakerFactory, restTemplate);
        ReflectionTestUtils.setField(myBookService, "url", "some awesome url");
        ReflectionTestUtils.setField(myBookService, "auth", "some auth");
        var token = "JSON Web token";
        var response = new ResponseEntity<String>(token, HttpStatus.OK);
        var expToken = "JSON Web token";

        doReturn(response).when(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class));

        var actToken = myBookService.getToken();

        assertThat(actToken).isEqualTo(expToken);
    }
}