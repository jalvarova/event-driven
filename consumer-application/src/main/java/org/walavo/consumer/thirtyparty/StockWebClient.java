package org.walavo.consumer.thirtyparty;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.walavo.consumer.thirtyparty.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class StockWebClient {

    @Autowired
    private WebClient inventoryWebApi;

    @Autowired
    private RetryTemplate retryTemplate;

    @CircuitBreaker(maxAttempts = 5, openTimeout = 15000L, resetTimeout = 30000L, include = {FeignException.class})
    public Mono<StockModel> callInventoryApi(StockModel stockModelRequest) {
        return retryTemplate.execute(retryContext -> inventoryWebApi
                .put()
                .uri("/stock/reserve")
                .body(Mono.just(stockModelRequest), StockModel.class)
                .retrieve()
                .bodyToFlux(StockModel.class)
                .next());
    }
}
