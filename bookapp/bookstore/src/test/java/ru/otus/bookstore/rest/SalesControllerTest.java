package ru.otus.bookstore.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ru.otus.bookstore.dto.BookDto;
import ru.otus.bookstore.dto.BookSaleDto;
import ru.otus.bookstore.service.SaleService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс SalesController должен")
class SalesControllerTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SaleService saleService;

    private SalesController salesController;

    @Test
    @DisplayName("получать список проданных книг")
    void shouldGetBookSales() {
        salesController = new SalesController(saleService, restTemplate);
        ReflectionTestUtils.setField(salesController, "url", "localhost:8080");
        ReflectionTestUtils.setField(salesController, "auth", "reader:pass");
        var books = new BookDto[]{new BookDto(1L, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"), "123-5-456-78901-2")};
        var response = new ResponseEntity<BookDto[]>(books, HttpStatus.OK);
        var token = new ResponseEntity<String>("fake token", HttpStatus.OK);
        var bookSales = List.of(new BookSaleDto("123-5-456-78901-2",
                LocalDate.of(2021, 7, 17), 5, new BigDecimal("250")));
        var expBookSale = new BookSaleDto("123-5-456-78901-2",
                LocalDate.of(2021, 7, 17), 5, new BigDecimal("250"));
        expBookSale.setBookTitle("На всякого мудреца довольно простоты");
        var expBookSales = List.of(expBookSale);

        doReturn(token).when(restTemplate).exchange(eq("http://localhost:8080/token"), eq(HttpMethod.POST), any(), eq(String.class));
        doReturn(response).when(restTemplate).exchange(eq("http://localhost:8080/mybooks/books"), eq(HttpMethod.GET), any(), eq(BookDto[].class));
        doReturn(bookSales).when(saleService).getAllSales();

        var actBookSales = salesController.getBookSales();

        assertThat(actBookSales).usingRecursiveFieldByFieldElementComparator().isEqualTo(expBookSales);
    }
}