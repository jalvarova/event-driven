# Rabbit MQ Started

## Init Configuration Environment

### Run Docker rabbit local

```docker
docker run -d --hostname local-rabbitmq --name local-rabbitmq -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### Build Package Project

```mvn
mvn clean package spring-boot:repackage
```

### Sprint Boot Stared

```mvn
mvn spring-boot:run
```

```java
package org.walavo.web.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebReactiveApplication.class, args);
    }
}
```


```java
@EnableJms
@Configuration
public class BrokerConfiguration {

    @Value("${rabbitmq.topic-exchange}")
    private String topicExchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKeyName;

    @Value("${rabbitmq.queue}")
    private String queueName;

    private static final String QUEUE_TYPE = "quorum";
    private static final String ARGUMENT_TYPE = "x-queue-type";


    @Bean
    public Queue queueProduct() {
        Queue queue = new Queue(queueName, Boolean.TRUE);
        queue.addArgument(ARGUMENT_TYPE, QUEUE_TYPE);
        return queue;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKeyName);
    }


    @Primary
    @Bean // Serialize message content to json using TextMessage
    public Jackson2JsonMessageConverter jacksonJmsMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```
```java
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
```

```java
@Configuration
@EnableWebFlux
public class WebReactiveConfiguration extends DelegatingWebFluxConfiguration {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("*")
                .allowCredentials(false);
    }
}
```
