package org.walavo.web.reactive.service;

import lombok.extern.slf4j.Slf4j;
import org.walavo.web.reactive.exceptions.NotFoundException;
import org.walavo.web.reactive.mappers.ProductMappers;
import org.walavo.web.reactive.model.entity.Product;
import org.walavo.web.reactive.model.repository.ProductRepository;
import org.walavo.web.reactive.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.walavo.web.reactive.controller.dto.ProductDTO;
import org.walavo.web.reactive.messaging.ProductPublisher;
import org.walavo.web.reactive.thirdparty.ProductWebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductWebClient productWebClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPublisher publisher;


    public Mono<ProductDTO> saveAsync(ProductDTO productDto) {
        return Mono.zip(
                Mono.just(publisher.sendMessage(productDto)),
                Mono.just(publisher.sendMessagePubSub(productDto)),
                (x, y) -> x)
                .doOnSubscribe(success -> log.info("Success save product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Mono<ProductDTO> save(ProductDTO productDto) {

        return Mono.just(productDto)
                .map(ProductMappers.apiToEntity)
                .map(productEntity -> productRepository.save(productEntity))
                .map(ProductMappers.entityToApiProduct)
                .doOnSubscribe(success -> log.info("Success save product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Mono<ProductDTO> update(ProductDTO productDto) {
        String skuCode = productDto.getSkuCode();
        return Mono.justOrEmpty(productRepository.findBySkuCode(skuCode))
                .switchIfEmpty(saveProductBySKu(skuCode))
                .map(productFound -> ProductUtil.foundProductUpdate(productDto, productFound))
                .map(productEntity -> productRepository.save(productEntity))
                .map(ProductMappers.entityToApiProduct)
                .doOnSubscribe(success -> log.info("Success update product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Flux<ProductDTO> findAll(List<String> skus) {
        return Flux.merge(getProductsApi(skus), getProductsEntity(skus))
                .subscribeOn(Schedulers.immediate())
                .doOnSubscribe(success -> log.info("Success findAll product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Mono<Product> saveProductBySKu(String skuCode) {
        return productWebClient.callProductApiBySku(skuCode)
                .map(ProductMappers.apiProductToEntity)
                .map(productEntity -> productRepository.save(productEntity))
                .doOnSubscribe(success -> log.info("Success find All product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Flux<ProductDTO> getProductsEntity(List<String> skus) {
        return Flux.just(productRepository.findBySkuCodes(skus))
                .flatMap(Flux::fromIterable)
                .map(ProductMappers.entityToApiProduct)
                .doOnSubscribe(success -> log.info("Success find All product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Flux<ProductDTO> getProductsApi(List<String> skus) {
        return productWebClient.callProductsApi(skus)
                .map(ProductMappers.mapperApiProduct)
                .doOnSubscribe(success -> log.info("Success Find All Api product"))
                .doOnError(exception -> log.error("Error occurred while consuming message", exception));
    }

    public Mono<ProductDTO> errorController(ProductDTO productDto) throws Exception {
        log.info("Error save product " + productDto);
        throw new NotFoundException("Terminó Goles");
    }

}
