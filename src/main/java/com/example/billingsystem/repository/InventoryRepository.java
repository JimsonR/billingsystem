package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query
    List<Inventory> findByProductIdProductId(Long id);// find by id gurthundha akkada adhi primary key lagutadhi kadha

    @Query(value = "SELECT * FROM inventory where LOWER(inventory.location) LIKE LOWER(CONCAT('%', :searchTerm , '%')) OR LOWER(inventory.supplier_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR CAST(inventory.stock_quantity as CHAR) LIKE CONCAT('%', :searchTerm , '%') ", nativeQuery = true)
    Page<Inventory> searchInventoryBySupAndLocAndQua(@Param(
            "searchTerm"
    ) String searchTerm , Pageable pageable);




}// ikkada productid anedhi foriegn key field ante product object aa product object lo unna id productid , ee id batti inventory table laganu
//idhe native query lo select * from inventory where product_id = 1 alaga anna maaata