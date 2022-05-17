package org.walavo.web.reactive.controller.dto;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class StockParameter {

    @Nullable
    private String skuCode;

    @Nullable
    private String saleEntity;

    @Nullable
    private String entities;
    
    @Nullable
    private Boolean validateSecurityStock;
}
