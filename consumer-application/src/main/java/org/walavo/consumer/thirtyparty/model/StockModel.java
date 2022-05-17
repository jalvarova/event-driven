package org.walavo.consumer.thirtyparty.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockModel implements Serializable {


    @NotNull(message = "Sku no puede estar vacío")
    @NotBlank(message = "Sku no puede estar vacío")
    private String skuCode;

    @NotNull(message = "Entidad no puede estar vacío")
    @NotBlank(message = "Entidad no puede estar vacío")
    private String entityCode;

    @NotNull(message = "Cantidad de items de producto no puede estar vacío")
    @Min(1L)
    private Double quantity;

    @NotNull(message = "Tipo de operacion de stock  no puede estar vacío")
    private StockOperation operation;

    @NotNull(message = "firma de reserva no puede estar vacío")
    @NotBlank(message = "firma de reserva no puede estar vacío")
    private String requestedBy;
}
