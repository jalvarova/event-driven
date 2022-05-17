# Rabbit MQ Consumer Steamer

## Init Configuration Environment

### Run Mongo local

```shell
cd mongo
docker-compose up -d
### Maven Build
```

### Build Package Project

```shell
mvn clean package spring-boot:repackage
```

### Sprint Boot Stared

```shell
mvn spring-boot:run
```

```postgresql
CREATE TABLE IF NOT EXISTS products (
   product_id bigserial,
   product_code VARCHAR(10),
   sku_code VARCHAR(10),
   company_code VARCHAR(10),
   description VARCHAR(255),
   internal_description VARCHAR(255),
   product_type VARCHAR(255),
   hierarchy_code VARCHAR(20),
   price DECIMAL,
   tax_sale CHAR(1),
   tax_selective DECIMAL,
   length DECIMAL,
   width DECIMAL,
   height DECIMAL,
   weight DECIMAL,
   dispatch_type VARCHAR(255),
   unit_measurement VARCHAR(255),
   united_nations_code VARCHAR(20),
   product_pmm_status CHAR(2)
 );
 
 ALTER TABLE products ADD CONSTRAINT product_pk PRIMARY KEY(product_id);

```


```java
package org.walavo.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
```

### Sprint Boot application.yml

```yaml
spring:
  cloud:
    stream:
      bindings:
        product:
          destination: local.product-exchange
          group: create_product_q
          consumer:
            max-attempts: 3
            acknowledge-mode: AUTO
            #dlx/dlq setup
            #auto-bind-dlq: true
            #dlq-ttl: 5000
            #dlq-dead-letter-exchange:
      #        output:
      #          destination: create_product_q
      #          binder: local.example.product.create
---
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: user
    password: password
---
management:
  endpoints:
    web:
      base-path: /
      path-mapping:
        prometheus: /metrics
      exposure:
        include:
          - health
          - info
          - metrics
          - prometheus
        exclude:
---
spring:
  data:
    mongodb:
      host: localhost
      username: ${username}
      password: ${password}

---
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:4432/product
    username: ${username}
    password: ${password}
    pool:
      initial-size: 1
      max-idle-time: 30s
      max-size: 5
      validation-query: "SELECT 1"
      max-create-connection-time: 30s
```

### Install Mongo Cli

```shell
sudo apt install mongodb-clients -y
```

### Mongo Operation commands

```mongo
mongo --host localhost:27017
show dbs
use product-db
db.products.find({})
```

## Publish Message Pub Sub


### Init project

```mongo
gcloud init
```

### created  topic Pub/sub

```mongo
gcloud pubsub topics create my-topic
```

### created subscriptor

```mongo
gcloud pubsub subscriptions create \
my-sub --topic my-topic \
--ack-deadline=60
```

### list pubsub  and subcriptor created

```mongo
gcloud pubsub topics list

gcloud pubsub subscriptions list
```

### Publish message on topic

```mongo
gcloud pubsub topics publish \
product-example --message "Hola mundo desde sdk gcp PubSub"
```

### Fetch messange in the subscriptor

```mongo
gcloud pubsub subscriptions pull \
--auto-ack --limit=2 my-sub

gcloud pubsub subscriptions create \
example-subscription --topic product-example \
--ack-deadline=60
```

# References

[Started RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

[Spring Cloud RabbitMQ](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream-binder-rabbit/2.2.0.M1/spring-cloud-stream-binder-rabbit.html#_rabbitmq_binder_overview)

[Docker Mongo](https://medium.com/faun/managing-mongodb-on-docker-with-docker-compose-26bf8a0bbae3)