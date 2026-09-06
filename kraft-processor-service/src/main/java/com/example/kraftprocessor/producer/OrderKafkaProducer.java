package com.example.kraftprocessor.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaProducer {

    private static final String TOPIC = "processed-orders";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String message) {
        log.info("Producing message to KRaft Kafka [{}]: key={}", TOPIC, key);
        kafkaTemplate.send(TOPIC, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to produce message to [{}]: {}", TOPIC, ex.getMessage(), ex);
                    } else {
                        log.info("Successfully produced message to [{}] partition={} offset={}",
                                TOPIC,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
