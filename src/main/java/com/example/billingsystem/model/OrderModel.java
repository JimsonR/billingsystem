package com.example.billingsystem.model;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.entity.Product;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    private Long id;

    private Customer customer;



    private List<Long> products;

    private BigDecimal totalPrice;

    private LocalDateTime orderDate;

    private String status;
}
