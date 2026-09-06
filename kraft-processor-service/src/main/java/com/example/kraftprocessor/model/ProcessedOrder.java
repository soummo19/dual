package com.example.kraftprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "processed_orders")
public class ProcessedOrder {

    @Id
    private String id;

    private String orderId;
    private String product;
    private int quantity;
    private double price;
    private String status;
    private Instant processedAt;
    private Instant createdAt;
}
