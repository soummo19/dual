package com.example.zkconsumer.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RestClientConfig {
    private final CloseableHttpClient httpClient;

    public RestClient restClient() {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}
