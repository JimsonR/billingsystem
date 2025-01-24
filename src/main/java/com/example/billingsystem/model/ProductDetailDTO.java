package com.example.billingsystem.model;

public interface ProductDetailDTO {

    Long getProductId();

    String  getName();

    String  getCategory();

    String getDescription();

    String getCreatedAt();

    String getUpdateAt();

    Boolean getIsActive();

}
