package com.example.billingsystem.model;

import com.example.billingsystem.entity.Orders;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerModel {

    private Long id;

    private String customerName;

    private String mobileNumber;

    private String emailId;

    private List<Long> ordersList;
}
