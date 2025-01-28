package com.example.billingsystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAndQuantity {
    ProductsList product;
    Long Quantity;

}
