package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.ProductDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

@Query(value = "SELECT * from product",nativeQuery = true)
    ProductDetailDTO[] getAllProducts();
//@Query("select p from Product p where "+
//"(:name is null OR p.name LIKE %:name5) AND" +
//"(:category is NULL OR p.category = :category)")
//
//List<Product> searchByNameAndCategory(
//        @Param("name") String name,
//        @Param("category") String category
//);
    @Query(value = "SELECT * FROM product where LOWER(product.name) LIKE LOWER(CONCAT('%', :searchTerm , '%')) OR LOWER(product.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR CAST(product.product_id as CHAR) LIKE CONCAT('%', :searchTerm , '%') ", nativeQuery = true)
    Page<Product> searchByNameAndCategory(@Param(
            "searchTerm"
    ) String searchTerm , Pageable pageable);

Optional<Product> findByNameAndCategory(String name , String category);




}
