package ru.otus.bookseller.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Sale {
    private String isbn;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate saleDate;
    private int quantity;
    private BigDecimal cost;
}
