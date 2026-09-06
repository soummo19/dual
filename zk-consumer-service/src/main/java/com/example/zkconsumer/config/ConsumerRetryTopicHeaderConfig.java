package com.example.zkconsumer.config;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.SingleRecordHeader;
import org.springframework.kafka.retrytopic.DeadLetterPublishingRecovererFactory;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;

import com.example.zkconsumer.utils.Constants;


@Configuration
@EnableKafka
public class ConsumerRetryTopicHeaderConfig extends RetryTopicConfigurationSupport {

    @Override
    protected Consumer<DeadLetterPublishingRecovererFactory> configureDeadLetterPublishingContainerFactory() {
        return factory -> factory.setHeadersFunction((consumerRecord, exception) -> {
            Headers headers = new RecordHeaders();
            headers.add(new SingleRecordHeader(
                                        Constants.HDR_FAILURE_REASON, 
                                        describeFailure(exception)
                                                .getBytes(StandardCharsets.UTF_8)));
            return headers;
        });
    }

    private String describeFailure(Exception exception) {
        Throwable cause = exception.getCause();
        String message = cause.getMessage();
        return StringUtils.isBlank(message)
            ? cause.getClass().getSimpleName()
            : cause.getClass().getSimpleName() + ": " + message;
    }
    
}
