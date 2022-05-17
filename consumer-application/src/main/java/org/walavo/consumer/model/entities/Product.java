package org.walavo.consumer.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
public class Product {

    @Id
    @Column("productId")
    private Long productId;

    @Column
    private String productCode;
    @Column
    private String skuCode;
    @Column
    private String productType;
    @Column
    private String description;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal weight;
    @Column
    private BigDecimal width;
    @Column
    private BigDecimal length;
    @Column
    private BigDecimal height;
    @Column
    private String unitMeasurement;
    @Column
    private String hierarchyCode;

}
