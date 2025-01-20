package com.example.billingsystem.model;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderResponseDTO {

    Long getOrderId();

    Customer getCustomer();

    Long getProductId();

    List<Product> getProducts();

    BigDecimal getTotalPrice();

    LocalDateTime getOrderDate();

    String getStatus();




}
