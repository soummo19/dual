package com.example.kraftprocessor.repository;

import com.example.kraftprocessor.model.ProcessedOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessedOrderRepository extends MongoRepository<ProcessedOrder, String> {

    List<ProcessedOrder> findByOrderId(String orderId);

    List<ProcessedOrder> findByStatus(String status);
}
