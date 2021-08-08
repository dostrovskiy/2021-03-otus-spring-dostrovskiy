package ru.otus.bookstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bookstore.domain.Sale;
import ru.otus.bookstore.dto.BookSaleDto;
import ru.otus.bookstore.repository.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс SaleServiceImpl должен")
class SaleServiceImplTest {
    private SaleService service;
    @Mock
    private SaleRepository repository;

    @Test
    @DisplayName("получать список продаж")
    void shouldGetAllSales() {
        service = new SaleServiceImpl(repository);
        var sale = Sale.builder()
                .id(1L)
                .isbn("123-5-456-78901-2")
                .saleDate(LocalDate.of(2021, 7, 17))
                .quantity(5).cost(new BigDecimal("250"))
                .build();
        var expBookSales = List.of(new BookSaleDto("123-5-456-78901-2",
                LocalDate.of(2021, 7, 17), 5, new BigDecimal("250")));

        doReturn(List.of(sale)).when(repository).findAll();

        var actBookSales = service.getAllSales();

        assertThat(actBookSales).usingRecursiveFieldByFieldElementComparator().isEqualTo(expBookSales);
    }

    @Test
    @DisplayName("сохранять продажи")
    void shouldSaveSale() {
        service = new SaleServiceImpl(repository);
        var sale = Sale.builder()
                .isbn("123-5-456-78901-2")
                .saleDate(LocalDate.of(2021, 7, 17))
                .quantity(5).cost(new BigDecimal("250"))
                .build();
        var expSale = Sale.builder()
                .id(1L)
                .isbn("123-5-456-78901-2")
                .saleDate(LocalDate.of(2021, 7, 17))
                .quantity(5).cost(new BigDecimal("250"))
                .build();

        doReturn(expSale).when(repository).save(sale);

        var actSale = service.saveSale(sale);

        assertThat(actSale).usingRecursiveComparison().isEqualTo(expSale);
    }
}