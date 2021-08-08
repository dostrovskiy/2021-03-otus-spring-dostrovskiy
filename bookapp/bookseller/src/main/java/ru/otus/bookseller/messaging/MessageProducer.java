package ru.otus.bookseller.messaging;

import ru.otus.bookseller.domain.Sale;

public interface MessageProducer {
    void send(Sale sale);
}
