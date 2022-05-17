package org.walavo.consumer.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.walavo.consumer.thirtyparty.StockFeignImpl;
import org.walavo.consumer.thirtyparty.StockWebClient;
import org.walavo.consumer.thirtyparty.model.StockModel;
import org.walavo.consumer.util.ConvertUtil;

import java.util.function.Consumer;

@Slf4j
@Component
public class StockEventLister {

    @Autowired
    private StockWebClient stockWebClient;

    @Autowired
    private StockFeignImpl stockFeign;

    @Bean
    public Consumer<Message<String>> eventStockReserve() {
        return this::apply;
    }

    private void apply(Message<String> stockPayload) {
        try {
            log.info("Event Init");
            String messageId = stockPayload.getHeaders().getId().toString();
            String idTransaction = (String) stockPayload.getHeaders().get("idTransaction");
            idTransaction = StringUtils.isEmpty(idTransaction) ? messageId : idTransaction;
            StockModel payload = ConvertUtil.stringToObject(stockPayload.getPayload(), StockModel.class);
            StockModel stockModelApi = stockFeign.inventoryApí(idTransaction, payload);
            log.info("Finished Event Api {}", ConvertUtil.jsonToString(stockModelApi));
        } catch (Exception exception) {
            log.error("Finish Error Event Handler");
        }
    }
}
