package org.walavo.consumer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class CircuitBreakerConfiguration {

    @Value("${retry.backoff}")
    private long backoffPeriod;

    @Value("${retry.max-attempts}")
    private int maxAttempts;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Value("${gcp.pubsub.error.topic}")
    private String topic;

    @Bean
    public RetryTemplate retryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(backoffPeriod); // retry interval 2000ms
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts); // retry 3 times
        retryTemplate.registerListener(new DefaultRetryListener(pubSubTemplate, topic));
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
