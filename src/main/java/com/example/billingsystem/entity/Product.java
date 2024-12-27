package com.example.billingsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "category" , nullable = false)
    private String category;

    @Column(name = "description")
    private String Description;

    @Column(name = "price" , nullable = false )
    private BigDecimal price;



}
