package com.example.zkconsumer.config;

import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Configuration
// public class KafkaConsumerPartitionLoggerConfig {
    
//     @Bean
//     public ConsumerAwareRebalanceListener consumerAwareRebalanceListener() {
//         return new ConsumerAwareRebalanceListener(){
            
//             @Override
//             public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
//                 log.info("Partitions revoked before commit: {}", partitions);
//             }

//             @Override
//             public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
//                 log.info("Partitions revoked after commit: {}", partitions);
//             }

//             @Override
//             public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
//                 log.info("Partitions assigned: {}", partitions);
//             }

//             @Override
//             public void onPartitionsLost(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
//                 log.info("Partitions lost: {}", partitions);
//             }
//         };
//     }
// }
