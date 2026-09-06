package com.example.zkconsumer.config;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class HttpClientConfig {
    private final HttpClientPoolConfig httpClientPoolConfig;


    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(httpClientPoolConfig.getConnectTimeout()))
                .build();
        
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(httpClientPoolConfig.getMaxTotal())
                .setMaxConnPerRoute(httpClientPoolConfig.getDefaultMaxPerRoute())
                .setDefaultConnectionConfig(connectionConfig)
                .build();
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (response, context) -> {
            BasicHeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator("Keep-Alive"));
            while (it.hasNext()) {
                HeaderElement he = it.next();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return TimeValue.ofMilliseconds(Long.parseLong(value)*1000);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid Keep-Alive timeout value: {}", value);  
                    }
            }
        }
            return TimeValue.ofMilliseconds(httpClientPoolConfig.getDefaultKeepAliveMillis());
        };
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connectionManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(httpClientPoolConfig.getRequestTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(httpClientPoolConfig.getResponseTimeout()))
                .build();
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean
    public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager connectionManager) {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay =10000 )
            public void run() {
                    try {
                        Thread.sleep(httpClientPoolConfig.getCloseIdleConnectionsWaitTimeSecs() * 1000);
                        connectionManager.closeExpired();
                        connectionManager.closeIdle(
                            TimeValue.ofSeconds(httpClientPoolConfig.getCloseIdleConnectionsWaitTimeSecs()));
                } catch (InterruptedException e) {
                    log.error("Idle connection monitor thread interrupted", e);
                }
            }
        };
    }
}