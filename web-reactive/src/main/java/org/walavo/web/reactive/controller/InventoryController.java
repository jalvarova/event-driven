package org.walavo.web.reactive.controller;


import org.walavo.web.reactive.controller.dto.StockModelRequest;
import org.walavo.web.reactive.controller.dto.StockParameter;
import org.walavo.web.reactive.messaging.InventoryPublisher;
import org.walavo.web.reactive.thirdparty.InventoryWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("api/v1")
public class InventoryController {

    @Autowired
    private InventoryPublisher inventoryPublisher;

    @Autowired
    private InventoryWebClient inventoryWebClient;

    @PostMapping(value = "/inventory/reserve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createEvent(@RequestBody StockModelRequest request) {
        return ResponseEntity.ok(inventoryPublisher.sendMessagePubSub(request));
    }

    @PostMapping(value = "/inventory/param", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createEvent(@RequestBody StockParameter parameter) {
        return ResponseEntity.ok(inventoryWebClient.callInventoryApi(parameter));
    }
}
