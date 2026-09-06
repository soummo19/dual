// package com.example.zkconsumer.config;

// import org.apache.kafka.clients.consumer.Consumer;
// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.slf4j.MDC;
// import org.springframework.kafka.listener.RecordInterceptor;
// import org.springframework.stereotype.Component;

// import com.example.zkconsumer.utils.Constants;
// import com.example.zkconsumer.utils.ZkConsumerServiceUtils;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class KafkaRecordTracingInterceptor implements RecordInterceptor<Object, Object> {

//     private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

//     @Override
//     public ConsumerRecord<Object, Object> intercept(ConsumerRecord<Object, Object> record, 
//                                                     Consumer<Object, Object> consumer) {
//         START_TIME.set(System.currentTimeMillis());
//         MDC.put(Constants.HDR_TRACE_ID, ZkConsumerServiceUtils.resolveTraceIdFromKafkaRecord(record));
//         MDC.put(Constants.STATE,
//                 record.topic().endsWith(Constants.DLT_SUFFIX) ? Constants.STATE_DEAD_LETTER
//                 :record.topic().contains(Constants.RETRY_SUFFIX) ? Constants.STATE_RETRY
//                                 : Constants.STATE_CONSUME);
//         return record;
//     }

//     @Override 
//     public void success(ConsumerRecord<Object, Object> record, Consumer<Object, Object> consumer) {
//         Long startTime = START_TIME.get();
//         if (startTime != null) {
//             MDC.put(Constants.STATE, Constants.SUCCESS);
//         }
//     }

//     @Override
//     public void failure(ConsumerRecord<Object, Object> record, Exception exception, Consumer<Object, Object> consumer) {
//         MDC.put(Constants.STATE, Constants.FAILURE);
//     }

//     @Override 
//     public void afterRecord(ConsumerRecord<Object, Object> record, Consumer<Object, Object> consumer) {
//         Long startTime = START_TIME.get();
//         if (startTime != null) {
//             long elapsedTime = System.currentTimeMillis() - startTime;
//             MDC.put(Constants.DURATION, String.valueOf(elapsedTime));
//         }
//         log.info("Finished processing record with key='{}' from topic='{}' in {} ms", record.key(), record.topic(), MDC.get(Constants.DURATION));
//         clearThreadState(consumer);
//     }

//     @Override 
//     public void clearThreadState(Consumer<?, ?> consumer) {
//         START_TIME.remove();
//         MDC.clear();
//     }

// }
