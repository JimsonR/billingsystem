package com.example.billingsystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductModel {
    public Long id;

    public String name;

    public String category;

    public String description;


}
