package ru.otus.bookseller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bookseller.domain.Sale;
import ru.otus.bookseller.messaging.MessageProducer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс SalesController должен")
class SalesControllerTest {

    @Mock
    private MessageProducer messageProducer;

    private SalesController salesController;

    @Test
    @DisplayName("добавлять данные о продаже книги")
    void shouldAddSale() {
        salesController = new SalesController(messageProducer);
        var sale = Sale.builder()
                .isbn("123-5-456-78901-2")
                .saleDate(LocalDate.of(2021, 7, 17))
                .quantity(5).cost(new BigDecimal("250"))
                .build();
        var saleCapture = ArgumentCaptor.forClass(Sale.class);

        Mockito.doNothing().when(messageProducer).send(saleCapture.capture());

        salesController.addSale(sale);

        assertThat(saleCapture.getValue()).usingRecursiveComparison().isEqualTo(sale);
    }
}