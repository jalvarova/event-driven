package org.walavo.consumer.listeners.transport;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String productCode;
    private String skuCode;
    private String productType;
    private String description;
    private BigDecimal price;
    private BigDecimal weight;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal height;
    private String unitMeasurement;
    private String hierarchyCode;
}
