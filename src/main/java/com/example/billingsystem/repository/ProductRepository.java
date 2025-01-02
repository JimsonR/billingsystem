package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.ProductDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

@Query(value = "SELECT * from product",nativeQuery = true)
    ProductDetailDTO[] getAllProducts();
}
