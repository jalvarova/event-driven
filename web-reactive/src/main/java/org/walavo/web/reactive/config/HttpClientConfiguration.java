package org.walavo.web.reactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HttpClientConfiguration {

    @Value("${product.uri}")
    private String uriProduct;


    @Value("${inventory.uri}")
    private String uriInventory;


    public WebClient productWebClient() {
        return WebClient
                .builder()
                .baseUrl(uriProduct)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public WebClient inventoryWebClient() {
        return WebClient
                .builder()
                .baseUrl(uriInventory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
