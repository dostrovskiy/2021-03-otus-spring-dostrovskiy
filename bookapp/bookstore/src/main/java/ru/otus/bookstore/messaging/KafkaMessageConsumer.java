package ru.otus.bookstore.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.bookstore.domain.Sale;
import ru.otus.bookstore.service.SaleService;

@Service
@RequiredArgsConstructor
public class KafkaMessageConsumer implements MessageConsumer {
    private final SaleService saleService;

    @KafkaListener(
            topics = "${bookstore.kafka-topic-name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void listen(Sale sale) {
        saleService.saveSale(sale);
    }
}
