package com.example.kraftprocessor.controller;

import com.example.kraftprocessor.model.Order;
import com.example.kraftprocessor.service.OrderProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping
    public ResponseEntity<Map<String, String>> receiveOrder(@RequestBody Order order) {
        log.info("Received order via REST: {}", order);
        orderProcessingService.processAndProduce(order);
        return ResponseEntity.ok(Map.of(
                "status", "ACCEPTED",
                "orderId", order.getId(),
                "message", "Order accepted and sent to KRaft Kafka for processing"
        ));
    }
}
