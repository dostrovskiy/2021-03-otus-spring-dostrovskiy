package ru.otus.bookstore.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bookstore.dto.BookDto;
import ru.otus.bookstore.dto.BookSaleDto;
import ru.otus.bookstore.exception.MyBooksConnectionException;
import ru.otus.bookstore.service.MyBookService;
import ru.otus.bookstore.service.SaleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class SalesController {
    private final SaleService saleService;
    private final MyBookService myBookService;

    @GetMapping("/book-sales")
    public List<BookSaleDto> getBookSales() {
        var isbnMap = getIsbnBookMap();
        var bookSales = saleService.getAllSales();
        if (!isbnMap.isEmpty())
            for (BookSaleDto bookSale : bookSales)
                if (isbnMap.get(bookSale.getIsbn()) != null)
                    bookSale.setBookTitle(isbnMap.get(bookSale.getIsbn()).getTitle());
        return bookSales;
    }

    @ExceptionHandler(MyBooksConnectionException.class)
    public ResponseEntity<String> handleBookReviewNotFound(MyBooksConnectionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private Map<String, BookDto> getIsbnBookMap() {
        var books = myBookService.getBooks();
        var map = new HashMap<String, BookDto>();
        for (BookDto book : books)
            map.put(book.getIsbn(), book);
        return map;
    }
}
