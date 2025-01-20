package com.example.billingsystem.model;

import com.example.billingsystem.entity.Orders;

import java.util.List;

public interface CustomerResponseDTO {

    String getId();

    String getCustomerName();

    String getMobileNumber();

    String getEmailId();

    List<Orders> getOrdersList();


}
