package com.example.zkconsumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZkConsumer {

    private static final String GROUP_ID = "${spring.kafka.consumer.group-id}";
    
    @RetryableTopic(
        attempts = "3",
        backOff  = @BackOff(delay = 5000, multiplier = 2.0),
        autoCreateTopics = "true",
        traversingCauses = "true",
        dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "${spring.kafka.topic.name.zk-topic1}", groupId = GROUP_ID)
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        log.info("received message='{}' with key='{}' partition='{}' from topic='{}'", consumerRecord.value(), consumerRecord.key(), consumerRecord.partition(), consumerRecord.topic());
    }
}
