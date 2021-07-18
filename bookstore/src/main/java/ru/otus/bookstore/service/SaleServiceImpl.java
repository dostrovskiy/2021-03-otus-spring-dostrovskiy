package ru.otus.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.bookstore.dto.BookSaleDto;
import ru.otus.bookstore.repository.SaleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;

    @Override
    public List<BookSaleDto> getAllSales() {
        return saleRepository.findAll().stream()
                .map(s -> new BookSaleDto(s.getIsbn(), s.getSaleDate(), s.getQuantity(), s.getCost()))
                .collect(Collectors.toList());
    }
}
