package com.example.kraftprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;
    private String product;
    private int quantity;
    private double price;
    private String timestamp;
}
