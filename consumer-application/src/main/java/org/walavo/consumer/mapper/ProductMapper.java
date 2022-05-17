package org.walavo.consumer.mapper;

import org.walavo.consumer.listeners.transport.ProductDto;
import org.walavo.consumer.model.entities.Product;
import org.walavo.consumer.model.entities.ProductDocument;

import java.util.function.Function;

@FunctionalInterface
public interface ProductMapper {

    void hello();

    Function<ProductDto, ProductDocument> eventToDocument= (ProductDto dto) ->
            ProductDocument
                    .builder()
                    .productCode(dto.getProductCode())
                    .skuCode(dto.getSkuCode())
                    .description(dto.getDescription())
                    .productType(dto.getProductType())
                    .price(dto.getPrice())
                    .weight(dto.getWeight())
                    .width(dto.getWidth())
                    .length(dto.getLength())
                    .height(dto.getHeight())
                    .unitMeasurement(dto.getUnitMeasurement())
                    .hierarchyCode(dto.getHierarchyCode())
                    .build();

    Function<ProductDto, Product> eventToEntity= (ProductDto dto) ->
            Product
                    .builder()
                    .productCode(dto.getProductCode())
                    .skuCode(dto.getSkuCode())
                    .description(dto.getDescription())
                    .productType(dto.getProductType())
                    .price(dto.getPrice())
                    .weight(dto.getWeight())
                    .width(dto.getWidth())
                    .length(dto.getLength())
                    .height(dto.getHeight())
                    .unitMeasurement(dto.getUnitMeasurement())
                    .hierarchyCode(dto.getHierarchyCode())
                    .build();
}
