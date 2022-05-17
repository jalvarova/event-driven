package org.walavo.web.reactive.thirdparty.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude()
public class InventoryModelApi {

    private String skuCode;
    private String entityCode;
    private List<StockDTO> stock = new ArrayList<>();
}
