package ru.otus.bookstore.rest;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.otus.bookstore.dto.BookDto;
import ru.otus.bookstore.dto.BookSaleDto;
import ru.otus.bookstore.exception.MyBooksConnectionException;
import ru.otus.bookstore.service.SaleService;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class SalesController {
    private final SaleService saleService;
    private final RestTemplate rest;
    private String token;
    @Value("${mybooks.url}")
    private String url;
    @Value("${mybooks.auth}")
    private String auth;

    @GetMapping("/book-sales")
    public List<BookSaleDto> getBookSales() {
        var isbnMap = getIsbnBookMap();
        var bookSales = saleService.getAllSales();
        for (BookSaleDto bookSale : bookSales)
            bookSale.setBookTitle(isbnMap.get(bookSale.getIsbn()).getTitle());
        return bookSales;
    }

    @ExceptionHandler(MyBooksConnectionException.class)
    public ResponseEntity<String> handleBookReviewNotFound(MyBooksConnectionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private Map<String, BookDto> getIsbnBookMap() {
        var books = getBooks();
        var map = new HashMap<String, BookDto>();
        for (BookDto book : books)
            map.put(book.getIsbn(), book);
        return map;
    }

    private List<BookDto> getBooks() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());
        try {
            var response = rest.exchange("http://" + url + "/mybooks/books",
                    HttpMethod.GET, new HttpEntity<String>(headers), BookDto[].class);
            return response.getStatusCode().equals(HttpStatus.OK) && (response.getBody() != null) ?
                    Arrays.asList(response.getBody()) :
                    Collections.emptyList();
        } catch (Exception e){
            throw new MyBooksConnectionException("Error getting a book list from MyBooks", e);
        }
    }

    private String getToken() {
        if (token == null) {
            var headers = new HttpHeaders();
            var encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
            headers.set("Authorization", "Basic " + new String(encodedAuth));
            var authEntity = new HttpEntity<>(headers);
            try {
                var response = rest.exchange("http://" + url + "/token",
                        HttpMethod.POST, authEntity, String.class);
                if (response.getStatusCode().equals(HttpStatus.OK) && (response.getBody() != null))
                    token = response.getBody();
            } catch (Exception e) {
                throw new MyBooksConnectionException("Error getting a token from MyBooks", e);
            }
        }
        return token;
    }
}
