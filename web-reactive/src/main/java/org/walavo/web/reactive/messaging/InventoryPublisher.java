package org.walavo.web.reactive.messaging;

import lombok.extern.slf4j.Slf4j;
import org.walavo.web.reactive.controller.dto.StockModelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;

import static org.walavo.web.reactive.util.ConvertUtil.jsonToString;

@Slf4j
@Component
public class InventoryPublisher extends PubSubComposite {

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Value("${spring.cloud.gcp.pubsub.inventory.topic}")
    private String topic;

    public StockModelRequest sendMessagePubSub(StockModelRequest stockModelRequest) {
        log.info("Begin publishSendMessage");
        String message = jsonToString(stockModelRequest);
        pubSubTemplate.publish(topic, message)
                .addCallback(getSuccessCallback(message), getFailureCallback(message));
        log.info("End publishSendMessage");
        return stockModelRequest;
    }
}
