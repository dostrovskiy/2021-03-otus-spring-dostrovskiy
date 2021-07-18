package ru.otus.bookstore.service;

import ru.otus.bookstore.dto.BookSaleDto;

import java.util.List;

public interface SaleService {
    List<BookSaleDto> getAllSales();
}
