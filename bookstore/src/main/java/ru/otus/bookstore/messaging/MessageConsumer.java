package ru.otus.bookstore.messaging;

import ru.otus.bookstore.domain.Sale;

public interface MessageConsumer {
    void listen(Sale sale);
}
