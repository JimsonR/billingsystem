package com.example.billingsystem.repository;

import com.example.billingsystem.entity.ProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalog , Long> {
}
