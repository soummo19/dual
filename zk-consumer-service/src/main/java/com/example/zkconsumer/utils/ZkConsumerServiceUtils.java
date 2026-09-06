package com.example.zkconsumer.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.core.NestedExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZkConsumerServiceUtils {

    public static String resolveTraceIdFromKafkaRecord(ConsumerRecord<?, ?> record) {
        Header traceIdHeader = record.headers().lastHeader(Constants.HDR_TRACE_ID);
        if(traceIdHeader!=null && traceIdHeader.value()!=null) {
            return new String(traceIdHeader.value(), StandardCharsets.UTF_8);
            
        }
        String traceId = generateUUID();
        record.headers().add(Constants.HDR_TRACE_ID, 
                                traceId.getBytes(StandardCharsets.UTF_8));
        return traceId;
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String describeFailure(Exception exception) {
        return NestedExceptionUtils.getMostSpecificCause(exception).getMessage();
    }
}
