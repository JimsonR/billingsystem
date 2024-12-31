package com.example.billingsystem.model;

public interface ProductDetailDTO {

    Long getProductID();

    String  getName();

    String  getCategory();

    String getDescription();

    String getCreatedAt();

    String getUpdateAt();

    Boolean getIsActive();

}
