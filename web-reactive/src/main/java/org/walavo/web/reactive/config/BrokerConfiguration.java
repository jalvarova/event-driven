package org.walavo.web.reactive.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;

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