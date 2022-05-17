package org.walavo.web.reactive.messaging;

import lombok.extern.slf4j.Slf4j;
import org.walavo.web.reactive.controller.dto.ProductDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;

import static org.walavo.web.reactive.util.ConvertUtil.jsonToString;

@Slf4j
@Component
public class ProductPublisher extends PubSubComposite {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Value("${spring.cloud.gcp.pubsub.product.topic}")
    private String topic;

    @Autowired
    private Binding binding;

    public ProductDTO sendMessage(ProductDTO productDto) {
        log.info("Rabbitmq Begin publishSendMessage");
        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), productDto);
        log.info("Rabbitmq End publishSendMessage");
        return productDto;
    }

    public ProductDTO sendMessagePubSub(ProductDTO productDto) {
        log.info("PubSub Begin publishSendMessage");
        String message = jsonToString(productDto);
        pubSubTemplate.publish(topic, message).addCallback(getSuccessCallback(message), getFailureCallback(message));
        log.info("PubSub End publishSendMessage");
        return productDto;
    }
}