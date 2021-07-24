package ru.otus.bookseller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bookseller.domain.Sale;
import ru.otus.bookseller.messaging.MessageProducer;

@RestController
@RequestMapping("/book-seller")
@RequiredArgsConstructor
public class SalesController {
    private final MessageProducer messageProducer;

    @PostMapping("/sale")
    public void addSale(@RequestBody Sale sale){
        messageProducer.send(sale);
    }
}
