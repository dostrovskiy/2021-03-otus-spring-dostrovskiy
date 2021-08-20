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
import ru.otus.bookstore.service.MyBookService;
import ru.otus.bookstore.service.SaleService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс SalesController должен")
class SalesControllerTest {
    @Mock
    private MyBookService myBookService;
    @Mock
    private SaleService saleService;

    private SalesController salesController;

    @Test
    @DisplayName("получать список проданных книг")
    void shouldGetBookSales() {
        salesController = new SalesController(saleService, myBookService);
        var books = List.of(new BookDto(1L, "На всякого мудреца довольно простоты",
                List.of("Островский А.Н."), List.of("Пьеса"), "123-5-456-78901-2"));
        var bookSales = List.of(new BookSaleDto("123-5-456-78901-2",
                LocalDate.of(2021, 7, 17), 5, new BigDecimal("250")));
        var expBookSale = new BookSaleDto("123-5-456-78901-2",
                LocalDate.of(2021, 7, 17), 5, new BigDecimal("250"));
        expBookSale.setBookTitle("На всякого мудреца довольно простоты");
        var expBookSales = List.of(expBookSale);

        doReturn(books).when(myBookService).getBooks();
        doReturn(bookSales).when(saleService).getAllSales();

        var actBookSales = salesController.getBookSales();

        assertThat(actBookSales).usingRecursiveFieldByFieldElementComparator().isEqualTo(expBookSales);
    }
}