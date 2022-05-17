package org.walavo.web.reactive.thirdparty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.walavo.web.reactive.config.HttpClientConfiguration;
import org.walavo.web.reactive.thirdparty.model.ProductModelApi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductWebClient extends TokenSession {

    @Autowired
    private HttpClientConfiguration httpConfig;

    public Flux<ProductModelApi> callProductsApi(List<String> skus) {
        return httpConfig
                .productWebClient()
                .post()
                .uri("/products")
                .header(HttpHeaders.AUTHORIZATION, getAuthenticationBasic())
                .body(Mono.just(skus), List.class)
                .retrieve()
                .bodyToFlux(ProductModelApi.class);
    }

    public Mono<ProductModelApi> callProductApiBySku(String skuCode) {
        return httpConfig
                .productWebClient()
                .get()
                .uri("/products/search/" + skuCode)
                .header(HttpHeaders.AUTHORIZATION, getAuthenticationBasic())
                .retrieve()
                .bodyToFlux(ProductModelApi.class)
                .next();
    }

}
