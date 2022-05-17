package org.walavo.web.reactive.thirdparty.model;

import lombok.Data;

@Data
public class StockDTO {

    /**
     * Entity Code
     * <p>
     *
     * (Required)
     *
     */
    private String entityCode;

    /**
     * Sku Code
     * <p>
     *
     * (Required)
     *
     */

    private String skuCode;
    /**
     * Quantity the available stock
     * <p>
     *
     * (Required)
     *
     */
    private Double quantity;
    /**
     * Current product price
     * <p>
     *
     *
     */
    private Double price;
    /**
     * Product cost
     * <p>
     *
     *
     */
    private Double cost;
    /**
     *
     */
    private String line;
}
