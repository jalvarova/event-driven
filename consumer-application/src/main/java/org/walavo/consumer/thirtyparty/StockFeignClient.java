package org.walavo.consumer.thirtyparty;

import org.walavo.consumer.thirtyparty.model.StockModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "identity-service", url = "${inventory.uri}")
public interface StockFeignClient {


    @RequestMapping(method = RequestMethod.PUT, value = "/stock/reserve")
    StockModel callInventoryApi(@RequestHeader(value = "idTransaction") String idTransaction, @RequestBody StockModel stockModelRequest);
}
