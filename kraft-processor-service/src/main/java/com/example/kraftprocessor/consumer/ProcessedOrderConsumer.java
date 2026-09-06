package com.example.kraftprocessor.consumer;

import com.example.kraftprocessor.model.Order;
import com.example.kraftprocessor.model.ProcessedOrder;
import com.example.kraftprocessor.repository.ProcessedOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessedOrderConsumer {

    private final ProcessedOrderRepository processedOrderRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "processed-orders", groupId = "kraft-processor-group")
    public void consume(String message) {
        log.info("Consumed message from KRaft Kafka [processed-orders]: {}", message);

        try {
            Order order = objectMapper.readValue(message, Order.class);

            ProcessedOrder processedOrder = ProcessedOrder.builder()
                    .orderId(order.getId())
                    .product(order.getProduct())
                    .quantity(order.getQuantity())
                    .price(order.getPrice())
                    .status("PROCESSED")
                    .processedAt(Instant.now())
                    .createdAt(Instant.now())
                    .build();

            ProcessedOrder saved = processedOrderRepository.save(processedOrder);
            log.info("Saved processed order to MongoDB: id={}, orderId={}", saved.getId(), saved.getOrderId());

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message: {}", e.getMessage(), e);
        }
    }
}
