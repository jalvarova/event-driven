package org.walavo.consumer.listeners;

import lombok.extern.slf4j.Slf4j;
import org.walavo.consumer.listeners.transport.ProductDto;
import org.walavo.consumer.model.entities.Product;
import org.walavo.consumer.model.entities.ProductDocument;
import org.walavo.consumer.model.repositories.ProductDocumentRepository;
import org.walavo.consumer.model.repositories.ProductRepository;
import org.walavo.consumer.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.walavo.consumer.mapper.ProductMapper;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
@Component
public class ProductEventListener {

    @Autowired
    private ProductDocumentRepository documentRepository;

    @Autowired
    private ProductRepository repository;

    @Bean
    public Consumer<Message<ProductDto>> eventProductSave() {
        return productPayload -> {
            log.info("Rabbitmq Event Init");
            String death = (String) productPayload.getHeaders().get("x-death");
            ProductDto productDto = productPayload.getPayload();
            //validateAttempt(getCountAttempt(death));
            documentRepository.findBySkuCode(productDto.getSkuCode())
                    .switchIfEmpty(Mono.just(saveMongoProductBySku(productDto)))
                    .map(productEntity -> productEntity)
                    .doOnSubscribe(success -> log.info("Success Event Consumer " + success))
                    .doOnError(exception -> log.error("Error occurred while consuming message ", exception))
                    .block();

            log.info("Rabbitmq Finished Event");
        };
    }


    @Bean
    public Consumer<Message<String>> eventPubsubProductSave() {
        return productPayload -> {
            log.info("PubSub Event Init");
            ProductDto productDto = ConvertUtil.stringToObject(productPayload.getPayload(), ProductDto.class);
            repository.findBySkuCode(productDto.getSkuCode())
                    .switchIfEmpty(Mono.just(saveProductBySku(productDto)))
                    .map(productEntity -> productEntity)
                    .doOnSubscribe(success -> log.info("Success Event Consumer " + success))
                    .doOnError(exception -> log.error("Error occurred while consuming message ", exception))
                    .block();

            log.info("PubSub Finished Event");
        };
    }

    private Product saveProductBySku(ProductDto dto) {

        return Stream.of(dto)
                .map(ProductMapper.eventToEntity)
                .map(s -> repository.save(s).block())
                .filter(Objects::nonNull)
                .peek(productDocument -> log.info("Success save product :" + productDocument.getProductCode()))
                .findAny()
                .orElseGet(Product::new);
    }

    private ProductDocument saveMongoProductBySku(ProductDto dto) {

        return Stream.of(dto)
                .map(ProductMapper.eventToDocument)
                .map(s -> documentRepository.save(s).block())
                .filter(Objects::nonNull)
                .peek(productDocument -> log.info("Success save product :" + productDocument.getProductCode()))
                .findAny()
                .orElseGet(ProductDocument::new);
    }
}
