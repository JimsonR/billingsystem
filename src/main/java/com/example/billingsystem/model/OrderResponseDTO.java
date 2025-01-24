package com.example.billingsystem.model;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface OrderResponseDTO {

    Long getOrderId();

    Long getId();

    String getCustomerName();

    String getMobileNumber();

    String getEmailId();

//    List<OrderResponseDTO> getOrdersList();


//    Long getProductId();

    Long getProductId();

    String  getName();

    String  getCategory();

    String getDescription();

    String getCreatedAt();

    String getUpdatedAt();

    Boolean getIsActive();


    BigDecimal getTotalPrice();

    LocalDateTime getOrderDate();

    String getStatus();




}
