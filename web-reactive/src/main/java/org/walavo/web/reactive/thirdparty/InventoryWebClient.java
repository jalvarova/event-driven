package org.walavo.web.reactive.thirdparty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.walavo.web.reactive.config.HttpClientConfiguration;
import org.walavo.web.reactive.controller.dto.StockParameter;
import org.walavo.web.reactive.thirdparty.model.InventoryModelApi;
import reactor.core.publisher.Mono;

@Component
public class InventoryWebClient extends TokenSession {

    @Autowired
    //@Qualifier("inventoryWebApi")
    private HttpClientConfiguration httpConfig;

    public Mono<InventoryModelApi> callInventoryApi(StockParameter stockParameter) {
        return httpConfig
                .inventoryWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stock")
                                .queryParam("skuCode", stockParameter.getSkuCode())
                                .queryParam("saleEntity", stockParameter.getSaleEntity())
                                .queryParam("entities", stockParameter.getEntities())
                                .queryParam("validateSecurityStock", stockParameter.getValidateSecurityStock())
                                .build()
                ).header(HttpHeaders.AUTHORIZATION, getInventoryAuthenticationBasic())
                .retrieve()
                .bodyToFlux(InventoryModelApi.class)
                .next();
    }


}
