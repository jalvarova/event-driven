package org.walavo.consumer.thirtyparty;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.walavo.consumer.thirtyparty.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockFeignImpl {

    @Autowired
    private StockFeignClient stockFeignClient;

    @Autowired
    private RetryTemplate retryTemplate;

    @CircuitBreaker(maxAttempts = 5, openTimeout = 15000L, resetTimeout = 30000L, include = {FeignException.class})
    public StockModel inventoryApí(String idTransaction, StockModel stockModelRequest) {
        return retryTemplate.execute(retryContext -> stockFeignClient.callInventoryApi(idTransaction, stockModelRequest));
    }

    //@Recover()
    public StockModel cacheFallbackResponse(StockModel stockModelRequest) {
        return StockModel
                .builder()
                .entityCode("OE-122")
                .skuCode("34015")
                .build();
    }
}
