package com.example.zkconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "http-client-pool")
@Getter @Setter
public class HttpClientPoolConfig {
    private int maxTotal;
    private int defaultMaxPerRoute;
    private int closeIdleConnectionsWaitTimeSecs;
    private int requestTimeout;
    private int defaultKeepAliveMillis;
    private int connectTimeout;
    private int responseTimeout;
}
