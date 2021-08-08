package ru.otus.bookseller.messaging;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.bookseller.domain.Sale;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer implements MessageProducer {
    private final KafkaTemplate<String, Sale> kafkaTemplate;

    @Value(value = "${bookseller.kafka-topic-name}")
    private String topicName;

    @SneakyThrows
    @Override
    public void send(Sale sale) {
        kafkaTemplate.send(topicName, sale);
    }
}
