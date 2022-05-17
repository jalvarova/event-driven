package org.walavo.web.reactive.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    @NotBlank(message = "El campo productCode no puede nulo o vació")
    @NotEmpty(message = "El campo productCode no puede nulo o vació")
    private String productCode;

    @NotBlank(message = "El campo skuCode no puede nulo o vació")
    @NotEmpty(message = "El campo skuCode no puede nulo o vació")
    private String skuCode;

    @NotBlank(message = "El campo productType no puede nulo o vació")
    @NotEmpty(message = "El campo productType no puede nulo o vació")
    private String productType;

    @NotBlank(message = "El campo description no puede nulo o vació")
    @NotEmpty(message = "El campo description no puede nulo o vació")
    private String description;

    @NotNull(message = "El campo price no puede nulo o vació")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "El campo weight no puede nulo o vació")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal weight;

    @NotNull(message = "El campo width no puede nulo o vació")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal width;

    @NotNull(message = "El campo length no puede nulo o vació")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal length;

    @NotNull(message = "El campo height no puede nulo o vació")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal height;

    @NotBlank(message = "El campo unitMeasurement no puede nulo o vació")
    @NotEmpty(message = "El campo unitMeasurement no puede nulo o vació")
    private String unitMeasurement;

    @NotBlank(message = "El campo hierarchyCode no puede nulo o vació")
    @NotEmpty(message = "El campo hierarchyCode no puede nulo o vació")
    private String hierarchyCode;
}
