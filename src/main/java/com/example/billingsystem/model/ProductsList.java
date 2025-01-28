package com.example.billingsystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductsList {

    private Long productId;
    private String name;
    private String category;
    private String description;
    private String createdAt;
    private String updateAt;
    private Boolean active;
}
