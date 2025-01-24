package com.example.billingsystem.model;

import com.example.billingsystem.entity.Orders;

import java.util.List;

public interface CustomerResponseDTO {

    Long getId();

    String getCustomerName();

    String getMobileNumber();

    String getEmailId();

    List<OrderResponseDTO> getOrdersList();


}
