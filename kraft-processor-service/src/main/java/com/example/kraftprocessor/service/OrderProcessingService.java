package com.example.kraftprocessor.service;

import com.example.kraftprocessor.model.Order;
import com.example.kraftprocessor.producer.OrderKafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final OrderKafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    public void processAndProduce(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            log.info("Processing order and sending to KRaft Kafka: {}", orderJson);
            kafkaProducer.sendMessage(order.getId(), orderJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process order", e);
        }
    }
}
