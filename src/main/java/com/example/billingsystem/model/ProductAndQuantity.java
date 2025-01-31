package com.example.billingsystem.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductAndQuantity {
    ProductsList product;
    Long Quantity;
    BigDecimal price;

}
