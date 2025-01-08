package com.example.billingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductModel {
    public Long id;

    @JsonIgnore
    public String name;

    @JsonIgnore
    public String category;

    @JsonIgnore
    public String description;


}
