package org.walavo.consumer.config;

import org.walavo.consumer.model.repositories.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackageClasses = ProductRepository.class)
public class R2dbcConfiguration {

}
