package org.walavo.consumer.model.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.walavo.consumer.model.entities.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("SELECT * FROM posts WHERE title like $1")
    Flux<Product> findBySkuCodeContains(String name);

    Mono<Product> findBySkuCode(String skuCode);
}
